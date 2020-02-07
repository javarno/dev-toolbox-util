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
package org.devtoolbox.util.exception;

import java.time.ZonedDateTime;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;


/**
 * @author Arnaud Lecollaire
 */
//TODO add javadoc
public class ParametricException extends Exception {

    private static final long serialVersionUID = 5188030268682980400L;

    private static final Logger LOGGER = LoggerFactory.getLogger(ParametricException.class);

    private final ZonedDateTime creationTime = ZonedDateTime.now();
    private final ErrorIdentifier identifier;
    private final Object[] parameters;


    public ParametricException(final ErrorIdentifier identifier) {
        this(identifier, null, (Object[]) null);
        LOGGER.debug("Initialized new parametric exception : " + this);
    }

    public ParametricException(final ErrorIdentifier identifier, final Throwable cause) {
        this(identifier, cause, (Object[]) null);
        LOGGER.debug("Initialized new parametric exception : " + this);
    }

    public ParametricException(final ErrorIdentifier identifier, final Object ... parameters) {
        super(MessageFormatter.arrayFormat(identifier.getDefaultMessage(), parameters).getMessage());
        this.identifier = identifier;
        this.parameters = parameters;
    }

    public ParametricException(final ErrorIdentifier identifier, final Throwable cause, final Object ... parameters) {
        super(MessageFormatter.arrayFormat(identifier.getDefaultMessage(), parameters).getMessage(), cause);
        this.identifier = identifier;
        this.parameters = parameters;
    }

    public ZonedDateTime getCreationTime() {
        return creationTime;
    }

    public ErrorIdentifier getIdentifier() {
        return identifier;
    }

    public Object[] getParameters() {
        return (parameters == null) ? null : Arrays.copyOf(parameters, parameters.length);
    }

    @Override
    public String toString() {
        final String classDate = getClass().getName() + " (" + creationTime + ')';
        final String message = getLocalizedMessage();
        return (message != null) ? (classDate + " : " + message) : classDate;
    }
}