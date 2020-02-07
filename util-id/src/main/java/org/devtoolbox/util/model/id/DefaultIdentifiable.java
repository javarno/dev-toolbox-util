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
package org.devtoolbox.util.model.id;

import java.util.Objects;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Arnaud Lecollaire
 */
public class DefaultIdentifiable implements Identifiable {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultIdentifiable.class);

    private final String id;


    public DefaultIdentifiable() {
        this(generateRandomUUID());
    }

    public DefaultIdentifiable(final String id) {
        super();
        this.id = id;
    }

    protected static String generateRandomUUID() {
        final String uuid = UUID.randomUUID().toString();
        LOGGER.trace("generated new random UUID : [{}]", uuid);
        return uuid;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean equals(final Object obj) {
        if (! (obj instanceof Identifiable)) {
            return false;
        }
        return Objects.equals(id, ((Identifiable) obj).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}