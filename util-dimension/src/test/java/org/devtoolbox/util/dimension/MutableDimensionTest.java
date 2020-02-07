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
package org.devtoolbox.util.dimension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;


public class MutableDimensionTest {


	@Test
	public void defaultValueTest() {
		final IntegerDimension dimension = new MutableIntegerDimensionModel();
		assertNull(dimension.getWidth());
		assertNull(dimension.getHeight());
	}

	@Test
	public void getterSetterTest() {
		final MutableIntegerDimensionModel dimension = new MutableIntegerDimensionModel();
		dimension.setDimension(Integer.MAX_VALUE, null);
		assertEquals((Integer) Integer.MAX_VALUE, dimension.getWidth());
		assertNull(dimension.getHeight());
		dimension.setDimension(null, Integer.MAX_VALUE);
		assertNull(dimension.getWidth());
		assertEquals((Integer) Integer.MAX_VALUE, dimension.getHeight());
		dimension.setDimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
		assertEquals((Integer) Integer.MAX_VALUE, dimension.getWidth());
		assertEquals((Integer) Integer.MAX_VALUE, dimension.getHeight());
		dimension.setDimension(new ImmutableIntegerDimension(Integer.MIN_VALUE, Integer.MIN_VALUE));
		assertEquals((Integer) Integer.MIN_VALUE, dimension.getWidth());
		assertEquals((Integer) Integer.MIN_VALUE, dimension.getHeight());
	}

	@Test
	public void propertiesSetterTest() {
		final MutableIntegerDimensionModel dimension = new MutableIntegerDimensionModel();
		dimension.setDimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
		final ObjectProperty<Integer> widthProperty = new SimpleObjectProperty<>();
		final ObjectProperty<Integer> heightProperty = new SimpleObjectProperty<>();
		dimension.bindBidirectionalWidthTo(widthProperty);
		dimension.bindBidirectionalHeightTo(heightProperty);
		// check that the properties are updated with current values of the dimension
		assertEquals((Integer) Integer.MAX_VALUE, widthProperty.get());
		assertEquals((Integer) Integer.MAX_VALUE, heightProperty.get());

		widthProperty.set(Integer.MIN_VALUE);
		assertEquals((Integer) Integer.MIN_VALUE, dimension.getWidth());
		heightProperty.set(Integer.MIN_VALUE);
		assertEquals((Integer) Integer.MIN_VALUE, dimension.getHeight());
		assertEquals(new ImmutableIntegerDimension(Integer.MIN_VALUE, Integer.MIN_VALUE), dimension.getDimension());
	}
}