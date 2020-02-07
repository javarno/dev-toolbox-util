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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Thread implementation that closes a resource implementing AutoCloseable when started.
 *
 * @author Arnaud Lecollaire
 */
//TODO provide several ways to handle errors
public class CleanerThread extends Thread {

	private static final Logger LOGGER = LoggerFactory.getLogger(CleanerThread.class);

	private final AutoCloseable resource;


	public CleanerThread(final AutoCloseable resource) {
		super();
		setName("cleaner thread for resource [" + resource + "]");

		this.resource = resource;
		LOGGER.debug("Created " + this + ".");
	}

	@Override
	public void run() {
	    LOGGER.debug("Closing resource [{}].", resource);

	    try {
	        resource.close();
	    } catch (final Exception error) {
	        LOGGER.error("Failed to close resource [{}].", resource, error);
	    }
	}

    @Override
    public String toString() {
        return "cleaner thread for resource [" + resource + "]";
    }
}