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

import static org.junit.jupiter.api.Assertions.fail;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

import org.devtoolbox.util.filesystem.FileSystemTools;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Arnaud Lecollaire
 */
//TODO add more tests
public class CleanerTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(CleanerTest.class);


	@Test
	public void cleanTest() {
		// TODO choose or implement another autocloseable resource to avoid using exception to detect if it has been closed
        performWithTestInputStream((input) -> {
            Cleaner.getInstance().addCleanerFor(input);
            Cleaner.getInstance().clean();
            // TODO use notification to know when cleaner have finish their work when implemented
            try {
                Thread.sleep(1000);
            } catch (final InterruptedException error) {
                error.printStackTrace();
                fail("Failed to wait for cleaner.");
            }
            try {
                input.available();
                fail("The stream should have throw an IOException.");
            } catch (final IOException error) {
                LOGGER.info("The test input stream has been closed as expected.");
            }
	    });
	}

	@Test
	public void removeCleanerTest() {
		// TODO choose or implement another autocloseable resource to avoid using exception to detect if it has been closed
        performWithTestInputStream((input) -> {
            final Thread cleanerThread = Cleaner.getInstance().addCleanerFor(input);
            Cleaner.getInstance().removeCleaner(cleanerThread);
            Cleaner.getInstance().clean();
            // TODO use notification to know when cleaner have finish their work when implemented
            try {
                Thread.sleep(1000);
            } catch (final InterruptedException error) {
                error.printStackTrace();
                fail("Failed to wait for cleaner.");
            }
            try {
                input.available();
                LOGGER.info("The test input stream had not been closed, as expected.");
            } catch (final IOException error) {
                error.printStackTrace();
                fail("The stream should not throw an IOException, the cleaner is not supposed to close it.");
            }
		});
	}

	protected void performWithTestInputStream(final Consumer<InputStream> consumer) {
	    try {
    	    FileSystemTools.doWithTemporaryFile("testCleaner", "tmp", testFile -> {
                try (FileInputStream input = new FileInputStream(testFile.toFile())) {
                    consumer.accept(input);
                } catch (final IOException error) {
                    error.printStackTrace();
                    fail("Failed to create test file input stream.");
                }
            });
    	} catch (final IOException error) {
    	    error.printStackTrace();
    	    fail("Unexpected I/O error occured during test.");
    	}
	}
}