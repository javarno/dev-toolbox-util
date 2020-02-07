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


/**
 * @author Arnaud Lecollaire
 */
public enum RecurrentTaskThreadStatus {
	/** thread has been created, but not initialized yet */
	CREATED(false),
	/** initialization failed, task will never be performed */
	INITIALIZATION_FAILED(true),
	/** thread is started, but it is not currently performing its task */
	RUNNING(false),
	/** thre thread is started and currently performing its task */
	PERFORMING_TASK(false),
    /** thread is started, but task execution is suspended */
    SUSPENDED(false),
	/** task is stopping, it will clear all used resources before reaching the STOPPED status */
	STOPPING(false),
	/** task is stopped */
	STOPPED(true);

    private final boolean finalState;


    private RecurrentTaskThreadStatus(final boolean finalState) {
        this.finalState = finalState;
    }

    public boolean isFinalState() {
        return finalState;
    }
}