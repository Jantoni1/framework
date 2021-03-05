/*
 * Copyright 2000-2021 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.v7.data.util.sqlcontainer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.Property;

/**
 * RowItem represents one row of a result set obtained from a QueryDelegate.
 *
 * Note that depending on the QueryDelegate in use this does not necessarily map
 * into an actual row in a database table.
 *
 * @deprecated As of 8.0, no replacement available.
 */
@Deprecated
public final class RowItem implements Item {
    private static final long serialVersionUID = -6228966439127951408L;
    private SQLContainer container;
    private RowId id;
    private Collection<ColumnProperty> properties;

    /**
     * Prevent instantiation without required parameters.
     */
    @SuppressWarnings("unused")
    private RowItem() {
    }

    public RowItem(SQLContainer container, RowId id,
            Collection<ColumnProperty> properties) {
        if (container == null) {
            throw new IllegalArgumentException("Container cannot be null.");
        }
        if (id == null) {
            throw new IllegalArgumentException("Row ID cannot be null.");
        }
        this.container = container;
        this.properties = properties;
        /* Set this RowItem as owner to the properties */
        if (properties != null) {
            for (ColumnProperty p : properties) {
                p.setOwner(this);
            }
        }
        this.id = id;
    }

    @Override
    public Property getItemProperty(Object id) {
        if (id instanceof String) {
            for (ColumnProperty cp : properties) {
                if (id.equals(cp.getPropertyId())) {
                    return cp;
                }
            }
        }
        return null;
    }

    @Override
    public Collection<?> getItemPropertyIds() {
        Collection<String> ids = new ArrayList<String>(properties.size());
        for (ColumnProperty cp : properties) {
            ids.add(cp.getPropertyId());
        }
        return Collections.unmodifiableCollection(ids);
    }

    /**
     * Adding properties is not supported. Properties are generated by
     * SQLContainer.
     */
    @Override
    public boolean addItemProperty(Object id, Property property)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * Removing properties is not supported. Properties are generated by
     * SQLContainer.
     */
    @Override
    public boolean removeItemProperty(Object id)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public RowId getId() {
        return id;
    }

    public SQLContainer getContainer() {
        return container;
    }

    public boolean isModified() {
        if (properties != null) {
            for (ColumnProperty p : properties) {
                if (p.isModified()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("ID:");
        s.append(getId());
        for (Object propId : getItemPropertyIds()) {
            s.append('|');
            s.append(propId);
            s.append(':');
            Object value = getItemProperty(propId).getValue();
            s.append(value);
        }
        return s.toString();
    }

    public void commit() {
        if (properties != null) {
            for (ColumnProperty p : properties) {
                p.commit();
            }
        }
    }
}
