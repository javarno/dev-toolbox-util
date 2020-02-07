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
package org.devtoolbox.util.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Utility class to handle system resources.
 *
 * @author Arnaud Lecollaire
 */
public class ResourceTools {

	private static final Logger LOGGER = LoggerFactory.getLogger(ResourceTools.class);

	public static final String URL_PROPERTY = "ResourceTools.PROPERTY_FILE_URL";


    /**
     * Reads properties from a file in the classpath (context class loader is used).
     * The property <code>ResourceTools.URL_PROPERTY</code> is added with the URL of the properties file found.
     *
     * @param filePath the path of the resource to load from the classpath
     * @return the requested properties
     * @throws ResourceException if the file can't be found or read
     */
    public static Properties readPropertiesFromClasspath(final String filePath) throws ResourceException {
        final URL propertiesFileURL = getClasspathResourceURL(filePath);
        if (propertiesFileURL == null) {
        	throw new ResourceException(ResourceError.FAILED_TO_FIND_RESOURCE, filePath, Thread.currentThread().getContextClassLoader());
        }
        final Properties properties = readProperties(propertiesFileURL);
        properties.put(URL_PROPERTY, propertiesFileURL);
		return properties;
    }

    /**
     * Reads properties from the specified URL.
     *
     * @param propertiesFileURL the URL of the properties to read
     * @return the requested properties
     * @throws ResourceException if the properties can't be read
     */
    public static Properties readProperties(final URL propertiesFileURL) throws ResourceException {
        if (propertiesFileURL == null) {
        	throw new IllegalArgumentException("propertiesFileURL can not be null");
        }
        LOGGER.info("Reading properties from [{}].", propertiesFileURL);
        final InputStream input = getClasspathResourceStream(propertiesFileURL);
        final Properties properties = new Properties();
    	try {
    		try {
    			properties.load(input);
    		} finally {
    			input.close();
    		}
    	} catch (final IllegalArgumentException error) {
    		throw new ResourceException(ResourceError.FAILED_TO_LOAD_PROPERTIES, error, propertiesFileURL);
    	} catch (final IOException error) {
    		throw new ResourceException(ResourceError.FAILED_TO_LOAD_PROPERTIES, error, propertiesFileURL);
    	}
		return properties;
    }

    /**
     * Reads properties from the specified input stream.
     *
     * @param input a stream from which the properties can be read
     * @return the requested properties
     * @throws ResourceException if the properties can't be read
     */
    public static Properties readProperties(final InputStream input) throws ResourceException {
    	final Properties properties = new Properties();
    	if (input == null) {
    		throw new IllegalArgumentException("input can not be null");
    	}
    	try {
    		try {
    			properties.load(input);
    		} finally {
    			input.close();
    		}
    	} catch (final IllegalArgumentException error) {
    		throw new ResourceException(ResourceError.FAILED_TO_LOAD_PROPERTIES, error, "provided input stream");
    	} catch (final IOException error) {
    		throw new ResourceException(ResourceError.FAILED_TO_LOAD_PROPERTIES, error, "provided input stream");
    	}
    	return properties;
    }

    /**
     * Gets the lcoation of a classpath resource using the current thread's context class loader.
     *
     * @param path the path of the resource to get inside the classpath
     * @return the location of the requested resource, or null if not found
     */
    public static URL getClasspathResourceURL(final String path) {
        final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		final URL resourceURL = contextClassLoader.getResource(path);
        if (resourceURL == null) {
        	LOGGER.info("Requested resource [{}] has not been found using current thread context class loader [{}].",
        			path, contextClassLoader);
        } else {
        	LOGGER.info("Requested resource [{}] has been found : [{}].", path, resourceURL);
        }
		return resourceURL;
    }

    /**
     * Opens a stream on a classpath resource using the current thread's context class loader.
     *
     * @param path the path of the resource to open inside the classpath
     * @return the opened stream
     * @throws ResourceException if the resource can't be found, or if the stream can't be opened
     */
    public static InputStream getClasspathResourceStream(final String path) throws ResourceException {
    	final URL resourceURL = getClasspathResourceURL(path);
    	if (resourceURL == null) {
    		throw new ResourceException(ResourceError.FAILED_TO_FIND_RESOURCE, path, Thread.currentThread().getContextClassLoader());
    	}
    	return getClasspathResourceStream(resourceURL);
    }

    /**
     * Opens a stream on a resource.
     *
     * @param resourceURL the URL of the resource to open
     * @return the opened stream
     * @throws ResourceException if the stream can't be opened
     */
    public static InputStream getClasspathResourceStream(final URL resourceURL) throws ResourceException {
    	if (resourceURL == null) {
    		throw new IllegalArgumentException("ResourceURL can not be null.");
    	}
    	LOGGER.info("Opening stream on resource [{}].", resourceURL);
    	try {
    		return resourceURL.openStream();
    	} catch (final IOException error) {
    		throw new ResourceException(ResourceError.FAILED_TO_OPEN_STREAM, error, resourceURL);
    	}
    }
}