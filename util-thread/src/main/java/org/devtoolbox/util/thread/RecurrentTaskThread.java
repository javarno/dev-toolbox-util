/*
 * MIT License
 *
 * Copyright © 2020 dev-toolbox.org
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.devtoolbox.util.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;


/**
 * <p>Thread implementation that repeatedly performs a task at regular intervals.</p>
 * <p>This implementation provides methods to control :</p>
 * <ul>
 *   <li>the time to wait between each task execution</li>
 *   <li>the state of the thread (running / suspended / stopped)</li>
 * </ul>
 *
 * @author Arnaud Lecollaire
 */
/*
 * TODO add delay mode (start to start or end to start)
 * TODO add an optional timeout when trying to close thread
 */
public abstract class RecurrentTaskThread extends NamedThread implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecurrentTaskThread.class);

    /** default delay used between check for thread status (in milliseconds) */
    private static final int DEFAULT_DELAY = 100;

    private final ReadOnlyObjectWrapper<RecurrentTaskThreadStatus> statusProperty = new ReadOnlyObjectWrapper<>(RecurrentTaskThreadStatus.CREATED);
    private final ReadOnlyBooleanWrapper stopAskedProperty = new ReadOnlyBooleanWrapper(false);
    private final ReadOnlyBooleanWrapper suspendedAskedProperty = new ReadOnlyBooleanWrapper(false);
    private Integer waitTime = null;


    public RecurrentTaskThread(final String threadName) {
        this(threadName, null);
    }

    public RecurrentTaskThread(final String threadName, final Integer waitTime) {
        super(threadName);
        setWaitTime(waitTime);
    }

    public ReadOnlyObjectProperty<RecurrentTaskThreadStatus> statusProperty() {
        return statusProperty.getReadOnlyProperty();
    }

    public ReadOnlyBooleanProperty stopAskedProperty() {
        return stopAskedProperty.getReadOnlyProperty();
    }

    public ReadOnlyBooleanProperty suspendedAskedProperty() {
        return suspendedAskedProperty.getReadOnlyProperty();
    }

    public void bindSuspendedAskedTo(final ObservableValue<? extends Boolean> suspendedAskedProperty) {
        this.suspendedAskedProperty.bind(suspendedAskedProperty);
    }

    @Override
    public final void run() {
        LOGGER.debug("Initializing recurrent task thread [{}].", getName());
        try {
            initialize();
        } catch (final RuntimeException error) {
            statusProperty.set(RecurrentTaskThreadStatus.INITIALIZATION_FAILED);
            handleError(error);
            // even if initialization failed, some resources might need to be closed
            try {
                doClose();
                return;
            } catch (final RuntimeException closeError) {
                handleError(closeError);
            }
        }

        LOGGER.debug("Recurrent task thread  [{}] is now running.", getName());
        statusProperty.set(RecurrentTaskThreadStatus.RUNNING);

        while (! stopAskedProperty.get()) {
            try {
                waitBeforeWork();
                if (statusProperty.get() == RecurrentTaskThreadStatus.SUSPENDED) {
                    if (suspendedAskedProperty.get()) {
                        continue;
                    }
                    statusProperty.set(RecurrentTaskThreadStatus.RUNNING);
                }
                if (suspendedAskedProperty.get()) {
                    statusProperty.set(RecurrentTaskThreadStatus.SUSPENDED);
                }
                if ((statusProperty.get() == RecurrentTaskThreadStatus.RUNNING) && (! stopAskedProperty.get())) {
                    statusProperty.set(RecurrentTaskThreadStatus.PERFORMING_TASK);
                    LOGGER.trace("Starting task [{}] execution.", getName());
                    try {
                        performTask();
                    } catch (final RuntimeException taskError) {
                        handleError(taskError);
                    } finally {
                        statusProperty.set(RecurrentTaskThreadStatus.RUNNING);
                    }
                }
            } catch (final RuntimeException error) {
                handleError(error);
            }
        }
        statusProperty.set(RecurrentTaskThreadStatus.STOPPING);

        LOGGER.debug("Closing recurrent task thread [{}].", getName());

        try {
            doClose();
        } catch (final RuntimeException error) {
            handleError(error);
        } finally {
            statusProperty.set(RecurrentTaskThreadStatus.STOPPED);
        }

        LOGGER.debug("{} closed.", this);
    }

    protected boolean isStopAsked() {
        return stopAskedProperty.get();
    }

    public RecurrentTaskThreadStatus getStatus() {
        return statusProperty.get();
    }

    protected void waitBeforeWork() {
        final Integer waitTime = getWaitTime();
        if (waitTime != null) {
            LOGGER.trace("{} is waiting [{}] milliseconds before next execution.", this, waitTime);
            try {
                Thread.sleep(waitTime);
            } catch (final InterruptedException error) {
                LOGGER.error("{} has been interrupted.", this, error);
            }
        }
    }

    /**
     * Gets the time to wait between two executions of this thread's task. If null, the thread will not pause.
     */
    protected Integer getWaitTime() {
        return waitTime;
    }

    /**
     * Sets the time to wait between two executions of this thread's task. If null, the thread will not pause.
     */
    protected void setWaitTime(final Integer waitTime) {
        this.waitTime = waitTime;
    }

    public void handleError(final RuntimeException error) {
        handleError(statusProperty.get(), error);
    }

    public void handleError(final RecurrentTaskThreadStatus status, final RuntimeException error) {
        LOGGER.error("An error occured in [{}] while in state [{}].", this, status, error);
    }

    protected void initialize() {}
    protected abstract void performTask();
    protected void doClose() {}

    /**
     * Sets the supsended state of the thread. It will wait until the thread is actually in the suspended or running state before returning.
     *
     * @throws IllegalStateException if the thread is stopped
     */
    public void setSuspended(final boolean suspended) {
        if (statusProperty.get().isFinalState()) {
            throw new IllegalStateException("The thread is in state [" + statusProperty.get() + "], it can not be suspended.");
        }
        setSuspendedAsked(suspended);
        final RecurrentTaskThreadStatus expectedStatus = suspended ? RecurrentTaskThreadStatus.SUSPENDED : RecurrentTaskThreadStatus.RUNNING;
        // wait until the thread is actually in the expected state
        // TODO add optional timeout
        while (getStatus() != expectedStatus) {
            try {
                Thread.sleep(DEFAULT_DELAY);
            } catch (final InterruptedException error) {
                LOGGER.error("{} has been interrupted while waiting for [{}] status.", this, expectedStatus, error);
            }
        }
    }

    /**
     * Asks the thread to suspend task execution. This method will not interrupt current execution, and will not wait until the thread is actually suspended.
     */
    public void setSuspendedAsked(final boolean suspendedAsked) {
        LOGGER.debug("Asking recurrent task thread [{}], hash = [" + this.hashCode() + "] to suspend execution.", getName());
        suspendedAskedProperty.set(suspendedAsked);
    }

    @Override
    public void close() {
        LOGGER.debug("Asking recurrent task thread [{}], hash = [" + this.hashCode() + "] to stop.", getName());
        if (statusProperty.get().isFinalState()) {
            throw new IllegalStateException("The recurrent task thread is in state [" + statusProperty.get() + "], it can not be closed.");
        }
        stopAskedProperty.set(true);

        LOGGER.debug("Waiting for recurrent task thread [{}] to stop.", getName());
        // TODO add an optional timeout
        while (statusProperty.get() != RecurrentTaskThreadStatus.STOPPED) {
            try {
                Thread.sleep(DEFAULT_DELAY);
            } catch (final InterruptedException error) {
                LOGGER.error("Recurrent task thread  [" + getName() + "] has been interrupted while waiting for stop.", error);
            }
        }
    }

    @Override
    public String toString() {
        return "RecurrentTaskThread [" + getName() + "], hash = [" + this.hashCode() + "]";
    }
}