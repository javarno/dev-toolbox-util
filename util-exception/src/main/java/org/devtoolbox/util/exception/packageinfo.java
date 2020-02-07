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
 * <p>This package provides extended implementations of the basic exception classes (Exception and RuntimeException) :<ul>
 *    <li>{@link ParametricException} includes parameters for logging or customizable error messages</li>
 *    <li>{@link ParametricRuntimeException} runtime version of the ParametricException</li>
 * </ul></p>
 * <p>The additional functionalities are :<ul>
 *    <li>an identifier enables to clearly identify the error</li>
 *    <li>additional data that can be used for the default error message or retrieved for error handling :<ul>
 *        <li>the creation time</li>
 *        <li>an optional collection of user-defined parameters</li>
 *    </ul>
 * </ul></p>
 * <p>The parametric exceptions uses the MessageFormatter from slf4j, which replaces {} by parameters.</p>
 *
 * @author Arnaud Lecollaire
 */
// TODO add tests
// TODO add exception UID ?
package org.devtoolbox.util.exception;