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
package org.devtoolbox.util.dimension;

import java.util.Objects;


/**
 * Immutable class for holding an integer dimension.
 * Both width and height can be null (that's the default value).
 * A null value means that the property is undefined.
 *
 * @author Arnaud Lecollaire
 */
public final class ImmutableIntegerDimension implements IntegerDimension {

	private Integer width = null;
	private Integer height = null;


	public ImmutableIntegerDimension(final Integer width, final Integer height) {
		super();
		this.width = width;
		this.height = height;
	}

	@Override
	public Integer getWidth() {
		return width;
	}

	@Override
	public Integer getHeight() {
		return height;
	}

	@Override
	public boolean equals(final Object other) {
		if (! (other instanceof ImmutableIntegerDimension)) {
			return false;
		}
		final ImmutableIntegerDimension otherDimnesion = (ImmutableIntegerDimension) other;
		return Objects.equals(width, otherDimnesion.width) && Objects.equals(height, otherDimnesion.height);
	}

	@Override
	public int hashCode() {
		return Objects.hash(width, height);
	}

	@Override
	public String toString() {
		return "immutable integer dimension [" + width + " x " + height + "]";
	}
}