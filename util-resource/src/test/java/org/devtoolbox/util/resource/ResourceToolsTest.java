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

/**
 * @author Arnaud Lecollaire
 */
package org.devtoolbox.util.resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.junit.jupiter.api.Test;


public class ResourceToolsTest {

	@Test
	public void getResourceURLTest() {
		assertNotNull(ResourceTools.getClasspathResourceURL("test 01"));
	}

	@Test
	public void getResourceURLWithPathTest() {
		assertNotNull(ResourceTools.getClasspathResourceURL("folder/test 02"));
	}

	@Test
	public void getResourceURLErrorTest() {
		assertNull(ResourceTools.getClasspathResourceURL("jhsdgfjdfjk"));
	}

	@Test
	public void getResourceURLWrongPathTest() {
		assertNull(ResourceTools.getClasspathResourceURL("test 02"));
	}

	@Test
	public void openStreamTest() {
		try {
			final InputStream stream = ResourceTools.getClasspathResourceStream("test 01");
			assertNotNull(stream);
			stream.close();
		} catch (ResourceException | IOException error) {
			error.printStackTrace();
			fail("The test resource should have been found and opened.");
		}
	}

	@Test
	public void openStreamErrorTest() {
		final String path = "jhsdgfjdfjk";
		try {
			ResourceTools.getClasspathResourceStream(path);
			fail("An error should have been thrown.");
		} catch (final ResourceException error) {
			assertEquals(ResourceError.FAILED_TO_FIND_RESOURCE, error.getIdentifier());
			assertEquals(path, error.getParameters()[0]);
		}
	}

	@Test
	public void openStreamFromNullURLTest() {
		try {
			 ResourceTools.getClasspathResourceStream((URL) null);
			fail("An error should have been thrown.");
		} catch (final IllegalArgumentException error) {
			// the IllegalArgumentException is an expected error
		} catch (final ResourceException error) {
			error.printStackTrace();
			fail("IllegalArgumentException should be thrown before the ResourceException.");
		}
	}

	@Test
	public void openStreamFromURLTest() {
		final URL resourceURL = ResourceTools.getClasspathResourceURL("folder/test 02");
		try {
			try (InputStream stream = ResourceTools.getClasspathResourceStream(resourceURL)) {}
		} catch (final ResourceException | IOException error) {
			error.printStackTrace();
			fail("An error should not have been thrown.");
		}
	}

	@Test
	public void openStreamFromWrongURLTest() {
		final URL resourceURL;
		try {
			resourceURL = new URL("file:///djhfgjdhfguj");
		} catch (final MalformedURLException error) {
			error.printStackTrace();
			fail("Test broken, URL should be valid.");
			return;
		}
		try {
			try (InputStream stream = ResourceTools.getClasspathResourceStream(resourceURL)) {}
			fail("An error should have been thrown.");
		} catch (final IOException error) {
			error.printStackTrace();
			fail("The error thrown should be a ResourceException.");
		} catch (final ResourceException error) {
			assertEquals(ResourceError.FAILED_TO_OPEN_STREAM, error.getIdentifier());
			assertEquals(resourceURL, error.getParameters()[0]);
		}
	}

	@Test
	public void readPropertiesTest() {
		try {
			final Properties properties = ResourceTools.readPropertiesFromClasspath("test 03.properties");
			assertEquals("testPropertyValue", properties.get("test.property"));
			// check the URL property added by ResourceTools
			final URL propertiesFileURL = (URL) properties.get(ResourceTools.URL_PROPERTY);
			assertNotNull(propertiesFileURL);
			try {
				try (InputStream input = ResourceTools.getClasspathResourceStream(propertiesFileURL)) {}
			} catch (final IOException error) {
				error.printStackTrace();
				fail("An error should not have been thrown.");
			}
		} catch (final ResourceException error) {
			error.printStackTrace();
			fail("An error should not have been thrown.");
		}
	}

	@Test
	public void readPropertiesWrongPathTest() {
		final String path = "test fdgdfg.properties";
		try {
			ResourceTools.readPropertiesFromClasspath(path);
			fail("An error should have been thrown.");
		} catch (final ResourceException error) {
			assertEquals(ResourceError.FAILED_TO_FIND_RESOURCE, error.getIdentifier());
			assertEquals(path, error.getParameters()[0]);
		}
	}

	@Test
	public void readPropertiesWrongFileTest() {
		final String path = "wrong.properties";
		try {
			ResourceTools.readPropertiesFromClasspath(path);
			fail("An error should have been thrown.");
		} catch (final ResourceException error) {
			assertEquals(ResourceError.FAILED_TO_LOAD_PROPERTIES, error.getIdentifier());
			final URL resourceURL = ResourceTools.getClasspathResourceURL(path);
			assertNotNull(resourceURL);
			assertEquals(resourceURL, error.getParameters()[0]);
		}
	}

	@Test
	public void readPropertiesWrongURLTest() {
		final URL resourceURL;
		try {
			resourceURL = new URL("file:///djhfgjdhfguj");
		} catch (final MalformedURLException error) {
			error.printStackTrace();
			fail("Test broken, URL should be valid.");
			return;
		}
		try {
			ResourceTools.readProperties(resourceURL);
			fail("An error should have been thrown.");
		} catch (final ResourceException error) {
			assertEquals(ResourceError.FAILED_TO_OPEN_STREAM, error.getIdentifier());
			assertEquals(resourceURL, error.getParameters()[0]);
		}
	}
}