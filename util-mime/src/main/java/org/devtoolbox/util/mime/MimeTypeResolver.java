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
package org.devtoolbox.util.mime;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Arnaud Lecollaire
 */
// TODO add asynchronous method
// TODO add javadoc / tests
public class MimeTypeResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(MimeTypeResolver.class);

    private static final String UNKNOWN_MIME_TYPE = "application/octet-stream";

    private static Tika tikaInstance;


    /**
     * Finds the mime type of the given file.
     *
     * @param file the file for which the mime type should be determined
     * @return the mime type, or <code>application/octet-stream</code> if none could be found
     * @throws IOException if an error occurs while trying to determine the file's type
     * @throws NullPointerException if file is null
     */
    public String getMimeType(final File file) throws IOException {
        Objects.requireNonNull(file);
        LOGGER.debug("Searching mime type for file [" + ((file == null) ? null : file.getAbsolutePath()) + "].");
        if (tikaInstance == null) {
            tikaInstance = new Tika();
        }
        String mimeType = tikaInstance.detect(file);
        mimeType = (mimeType == null) ? UNKNOWN_MIME_TYPE : mimeType;
        LOGGER.debug("Mime type found for file [" + file.getAbsolutePath() + "] : [" + mimeType +"].");
        return mimeType;
    }
}
