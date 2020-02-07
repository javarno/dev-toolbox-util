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
package org.devtoolbox.util.model.change;

import org.devtoolbox.util.model.ChildModificationType;
import org.devtoolbox.util.model.PropertyTypeDescriptor;


/**
 * @author Arnaud Lecollaire
 */
public class ChildPropertyTypedChange<PropertyType extends PropertyTypeDescriptor, ValueType> extends ChildPropertyChange<PropertyType> {


    public ChildPropertyTypedChange(final PropertyType property, final ChildModificationType childModificationType,
            final ValueType oldValue, final ValueType newValue) {

        super(property, childModificationType, oldValue, newValue);
    }

    @SuppressWarnings("unchecked") // cast is safe since the only way to modify new value is using constructor, where the type is checked
    @Override
    public ValueType getNewValue() {
        return (ValueType) super.getNewValue();
    }

    @SuppressWarnings("unchecked") // cast is safe since the only way to modify old value is using constructor, where the type is checked
    @Override
    public ValueType getOldValue() {
        return (ValueType) super.getOldValue();
    }
}
