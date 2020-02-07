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
package org.devtoolbox.util.clean;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>Utility class to close a set of resources, typically when closing an application.</p>
 *
 * <p>A static instance is available using <code>getInstance()</code>, and a public constructor is available
 * in case several sets of resources needs to be closed at different times.</p>
 *
 * <p>Each instance contains a collection of threads that are started when the <code>close()</code> method is called.</p>
 *
 * @author Arnaud Lecollaire
 */
// TODO provide several ways to handle errors
// TODO add something to be notified when cleaner threads have finished their work, with timeout
public class Cleaner {

	private static final Logger LOGGER = LoggerFactory.getLogger(Cleaner.class);
    private static final Cleaner INSTANCE = new Cleaner();

    private final Collection<Thread> cleanerThreads = new ArrayList<>();


    /**
     * Creates a new <code>Cleaner</code>.
     */
    public Cleaner() {
        super();
        LOGGER.debug("New Cleaner initialized.");
    }

    /**
     * Gets the static <code>Cleaner</code> instance.
     *
     * @return the static <code>Cleaner</code> instance
     */
    public static Cleaner getInstance() {
        return INSTANCE;
    }

    /**
     * Adds a cleaner thread. It will be started when the <code>close()</code> method is called, unless it is removed before that.
     *
     * @param cleanerThread the cleaner thread to add
     */
    public synchronized void addCleanerThread(final Thread cleanerThread) {
    	LOGGER.info("Adding cleaner thread [{}].", cleanerThread);
        cleanerThreads.add(cleanerThread);
    }

    /**
     * Adds a cleaner thread for the given resource. It will be started when the <code>close()</code> method is called, unless it is removed before that.
     *
     * @param resource the resource for which a cleaner thread should be added
     * @return the added cleaner thread
     */
    // TODO check if there is already a cleaner for this resource
    public synchronized Thread addCleanerFor(final AutoCloseable resource) {
        LOGGER.info("Adding cleaner for resource [{}].", resource);

        final Thread cleaner = new CleanerThread(resource);
		cleanerThreads.add(cleaner);
		return cleaner;
    }

    /**
     * Removes a cleaner thread that was previously registered.
     *
     * @param cleanerThread the cleaner thread to remove
     * @return true if the cleaner thread has been removed, false otherwise (if the thread had not been added or was already removed)
     */
    public synchronized boolean removeCleaner(final Thread cleanerThread) {
    	LOGGER.info("Removing cleaner thread [{}].", cleanerThread);

    	return cleanerThreads.remove(cleanerThread);
    }

    /**
     * Starts all registered cleaner threads.
     */
    public synchronized void clean() {
        LOGGER.info("Cleaner has been asked to start all registered cleaner threads ({}).", cleanerThreads.size());

        cleanerThreads.forEach((cleanerThread) -> {
            LOGGER.info("Starting [{}].", cleanerThread);
            try {
                cleanerThread.start();
            } catch (final RuntimeException error) {
                LOGGER.error("Failed to start cleaner thread [{}].", cleanerThread, error);
            }
        });
    }
}