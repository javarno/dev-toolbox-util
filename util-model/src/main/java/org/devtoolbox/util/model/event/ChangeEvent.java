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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EventObject;

import org.devtoolbox.util.model.PropertyTypeDescriptor;
import org.devtoolbox.util.model.change.PropertyChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Event class including a set of modified properties.
 *
 * @author Arnaud Lecollaire
 * @param <SourceType> the type of source of the event
 * @param <PropertyChangeType> the type of property change
 */
public class ChangeEvent<SourceType,
        PropertyType extends PropertyTypeDescriptor,
        PropertyChangeType extends PropertyChange<PropertyType>
> extends EventObject {


    private static final long serialVersionUID = 4585218680328439886L;

    private static final Logger LOGGER = LoggerFactory.getLogger(ChangeEvent.class);

    private final Collection<? extends PropertyChangeType> propertyChanges;


    /**
     * @throws IllegalArgumentException if source is null
     */
	public ChangeEvent(final SourceType source) {
		super(source);
		this.propertyChanges = Collections.emptyList();
		LOGGER.debug("created new [{}]", this);
	}

	/**
	 * @throws IllegalArgumentException if source is null
	 */
	public ChangeEvent(final SourceType source, final Collection<? extends PropertyChangeType> propertyChanges) {
		super(source);
		this.propertyChanges = new ArrayList<>(propertyChanges);
		LOGGER.debug("created new [{}]", this);
	}

	@SafeVarargs
    public ChangeEvent(final SourceType source, final PropertyChangeType ... propertyChanges) {
		this(source, Arrays.asList(propertyChanges));
	}

	// check is safe since the constructor is then the only way to set the source,
	// and the type is checked there
	@SuppressWarnings("unchecked")
	@Override
	public SourceType getSource() {
		return (SourceType) super.getSource();
	}

	public Collection<? extends PropertyChangeType> getPropertyChanges() {
		return Collections.unmodifiableCollection(propertyChanges);
	}

	@Override
	public String toString() {
		return ("Change event for source [" + getSource() + "] ("
				+ (propertyChanges.size()) + " propert(y/ies) modified).");
	}
}