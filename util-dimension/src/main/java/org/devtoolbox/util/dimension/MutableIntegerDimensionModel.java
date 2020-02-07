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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;


/**
 * Mutable implementation of <code>IntegerDimension</code>, using javafx beans for modification listeners.
 *
 * @author Arnaud Lecollaire
 */
public class MutableIntegerDimensionModel implements IntegerDimension {

	private static final Logger LOGGER = LoggerFactory.getLogger(MutableIntegerDimensionModel.class);

	private final ObjectProperty<Integer> widthProperty = new SimpleObjectProperty<>();
	private final ObjectProperty<Integer> heightProperty = new SimpleObjectProperty<>();
	private final ObjectProperty<ImmutableIntegerDimension> dimensionProperty = new SimpleObjectProperty<>();
	private final Object updateDimensionsLock = new Object();
	private volatile boolean updatingDimension = false;


	public MutableIntegerDimensionModel() {
		super();
		widthProperty.addListener((arg0, arg1, newWidth) -> {
			synchronized (updateDimensionsLock) {
				if (updatingDimension) {
					return;
				}
			}
			updateDimensionProperty(new ImmutableIntegerDimension(newWidth, getHeight()));
		});
		heightProperty.addListener((arg0, arg1, newHeight) -> {
			synchronized (updateDimensionsLock) {
				if (updatingDimension) {
					return;
				}
			}
			updateDimensionProperty(new ImmutableIntegerDimension(getWidth(), newHeight));
		});
	}

	/**
	 * Sets both properties of this dimension. Dimension listeners will only receive one notification if both properties are modified.
	 */
	public void setDimension(final Integer newWidth, final Integer newHeight) {
		internalSetDimension(newWidth, newHeight);
	}

	public void setDimension(final ImmutableIntegerDimension newDimension) {
		internalSetDimension(newDimension.getWidth(), newDimension.getHeight());
	}

	/**
	 * Sets dimension property for width, height and dimension internal properties.
	 * All modifications of the dimension property should be done exclusively by this method to handle concurrent access.
	 */
	protected void internalSetDimension(final Integer newWidth, final Integer newHeight) {
		synchronized (updateDimensionsLock) {
			updatingDimension = true;
			try {
				final Integer oldWidth = this.widthProperty.get();
				final Integer oldHeight = this.heightProperty.get();
				boolean sizeChanged = false;
				if (! Objects.equals(oldWidth, newWidth)) {
					LOGGER.info("Updating width from [{}] to [{}].", oldWidth, newWidth);
					this.widthProperty.set(newWidth);
					sizeChanged = true;
				}
				if (! Objects.equals(oldHeight, newHeight)) {
					LOGGER.info("Updating height from [{}] to [{}].", oldHeight, newHeight);
					this.heightProperty.set(newHeight);
					sizeChanged = true;
				}
				if (sizeChanged) {
					updateDimensionProperty(new ImmutableIntegerDimension(newWidth, newHeight));
				}
			} finally {
				updatingDimension = false;
			}
		}
	}

	/**
	 * Sets dimension property value alone (should only be used internally when width or height properties are updated
	 * to synchronize the value of the internal dimension properties).
	 */
	protected void updateDimensionProperty(final ImmutableIntegerDimension newDimension) {
		LOGGER.info("Updating dimension from [{}] to [{}].", dimensionProperty.get(), newDimension);
		dimensionProperty.set(newDimension);
	}

	@Override
	public Integer getWidth() {
		return widthProperty.get();
	}

	@Override
	public Integer getHeight() {
		return heightProperty.get();
	}

	/**
	 * Gets the current value of this dimension.
	 */
	public ImmutableIntegerDimension getDimension() {
		return dimensionProperty.get();
	}

	public void addWidthModificationListener(final ChangeListener<? super Integer> listener) {
		widthProperty.addListener(listener);
	}

	public void addHeightModificationListener(final ChangeListener<? super Integer> listener) {
		heightProperty.addListener(listener);
	}

	/**
	 * Binds an integer property to this dimension's width.
	 * The value of the property will be set to the current width of this dimension before it is bound.
	 */
	public void bindBidirectionalWidthTo(final ObjectProperty<Integer> widthProperty) {
		widthProperty.set(this.widthProperty.get());
		Bindings.bindBidirectional(this.widthProperty, widthProperty);
	}

	/**
	 * Binds an integer property to this dimension's height.
	 * The value of the property will be set to the current height of this dimension before it is bound.
	 */
	public void bindBidirectionalHeightTo(final ObjectProperty<Integer> heightProperty) {
		heightProperty.set(this.heightProperty.get());
		Bindings.bindBidirectional(this.heightProperty, heightProperty);
	}

	/**
	 * Adds a listener that will be notified if one (or both) of the dimension properties is modified.
	 */
	public void addDimensionListener(final ChangeListener<ImmutableIntegerDimension> listener) {
		this.dimensionProperty.addListener(listener);
	}
}