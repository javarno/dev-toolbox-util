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
package org.devtoolbox.util.model.event;

import java.util.Collection;

import org.devtoolbox.util.model.PropertyTypeDescriptor;
import org.devtoolbox.util.model.change.PropertyTypedChange;


/**
 * Event class including a set of modified properties.
 *
 * @author Arnaud Lecollaire
 * @param <SourceType> the type of source of the event
 * @param <PropertyChangeType> the type of property change
 */
public class TypedChangeEvent<SourceType,
        PropertyType extends PropertyTypeDescriptor,
        ValueType,
        PropertyChangeType extends PropertyTypedChange<PropertyType, ValueType>
> extends ChangeEvent<SourceType, PropertyType, PropertyChangeType> {

    private static final long serialVersionUID = 4585218680328439886L;


    /**
     * @throws IllegalArgumentException if source is null
     */
	public TypedChangeEvent(final SourceType source) {
		super(source);
	}

	/**
	 * @throws IllegalArgumentException if source is null
	 */
	public TypedChangeEvent(final SourceType source, final Collection<? extends PropertyChangeType> propertyChanges) {
		super(source, propertyChanges);
	}

	@SafeVarargs
    public TypedChangeEvent(final SourceType source, final PropertyChangeType ... propertyChanges) {
	    super(source, propertyChanges);
	}
}