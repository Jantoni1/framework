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
package com.vaadin.ui.components.grid;

import java.util.stream.Stream;

import com.vaadin.data.provider.QuerySortOrder;
import com.vaadin.server.SerializableFunction;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.ui.Grid.Column;

/**
 * Generates the sort orders when rows are sorted by a column.
 *
 * @see Column#setSortOrderProvider
 *
 * @since 8.0
 * @author Vaadin Ltd
 */
@FunctionalInterface
public interface SortOrderProvider
        extends SerializableFunction<SortDirection, Stream<QuerySortOrder>> {

    /**
     * Generates the sort orders when rows are sorted by a column.
     *
     * @param sortDirection
     *            desired sort direction
     *
     * @return sort information
     */
    @Override
    public Stream<QuerySortOrder> apply(SortDirection sortDirection);

}
