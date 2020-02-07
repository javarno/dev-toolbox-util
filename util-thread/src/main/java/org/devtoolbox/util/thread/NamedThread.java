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

import java.util.Objects;


/**
 * Simple thread extension using the thread name (and hashcode) in the toString method.
 *
 * @author Arnaud Lecollaire
 */
public class NamedThread extends Thread {


    /**
     * Constructs a new <code>CloseableThread</code> using the specified name.
     *
     * @param threadName the thread name that will ne used in toString later
     * @throws NullPointerException if threadName is null
     * @see Thread#setName(String)
     */
    public NamedThread(final String threadName) {
        super();
        Objects.requireNonNull(threadName);
        setName(threadName);
    }

    @Override
    public String toString() {
        return "Thread [" + getName() + "], hash = [" + this.hashCode() + "]";
    }
}
