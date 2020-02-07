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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;


/**
 * @author Arnaud Lecollaire
 */
//TODO add test for creationTime
//TODO add test for cause
public class ParametricExceptionTest {


	@Test
	public void identifierTest() {
		try {
			throwException(TestErrorIdentifier.TEST1);
			fail("An exception should have been thrown.");
		} catch (final ParametricException error) {
			assertEquals(TestErrorIdentifier.TEST1, error.getIdentifier());
			assertNull(error.getParameters());
			assertEquals(TestErrorIdentifier.TEST1.getDefaultMessage(), error.getMessage());
		}
	}

	@Test
	public void parametersTest() {
		final String firstParameter = "test parameters";
		final Object otherParameter = new Object();
		try {
			throwException(TestErrorIdentifier.TEST2, firstParameter, otherParameter);
			fail("An exception should have been thrown.");
		} catch (final ParametricException error) {
			assertEquals(TestErrorIdentifier.TEST2, error.getIdentifier());
			final Object[] parameters = error.getParameters();
			assertEquals(firstParameter, parameters[0]);
			assertTrue(otherParameter == parameters[1]);
			assertEquals(TestErrorIdentifier.TEST2.getDefaultMessage().replaceAll("\\{\\}", firstParameter), error.getMessage());
		}
	}

	protected void throwException(final ErrorIdentifier errorIdentifier) throws ParametricException {
		throw new ParametricException(errorIdentifier);
	}

	protected void throwException(final ErrorIdentifier errorIdentifier, final Object ... parameters) throws ParametricException {
		throw new ParametricException(errorIdentifier, parameters);
	}
}