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
package com.vaadin.data.provider;

import java.lang.reflect.Method;
import java.util.EventObject;

import com.vaadin.data.provider.DataChangeEvent.DataRefreshEvent;
import com.vaadin.event.EventRouter;
import com.vaadin.shared.Registration;

/**
 * Abstract data provider implementation which takes care of refreshing data
 * from the underlying data provider.
 *
 * @param <T>
 *            data type
 * @param <F>
 *            filter type
 *
 * @author Vaadin Ltd
 * @since 8.0
 *
 */
public abstract class AbstractDataProvider<T, F> implements DataProvider<T, F> {

    private EventRouter eventRouter;

    @Override
    public Registration addDataProviderListener(
            DataProviderListener<T> listener) {
        return addListener(DataChangeEvent.class, listener,
                DataProviderListener.class.getMethods()[0]);
    }

    @Override
    public void refreshAll() {
        fireEvent(new DataChangeEvent<>(this));
    }

    @Override
    public void refreshItem(T item) {
        fireEvent(new DataRefreshEvent<>(this, item));
    }

    /**
     * Registers a new listener with the specified activation method to listen
     * events generated by this component. If the activation method does not
     * have any arguments the event object will not be passed to it when it's
     * called.
     *
     * @param eventType
     *            the type of the listened event. Events of this type or its
     *            subclasses activate the listener.
     * @param listener
     *            the object instance who owns the activation method.
     * @param method
     *            the activation method.
     * @return a registration for the listener
     */
    protected Registration addListener(Class<?> eventType,
            DataProviderListener<T> listener, Method method) {
        if (eventRouter == null) {
            eventRouter = new EventRouter();
        }
        return eventRouter.addListener(eventType, listener, method);
    }

    /**
     * Sends the event to all listeners.
     *
     * @param event
     *            the Event to be sent to all listeners.
     */
    protected void fireEvent(EventObject event) {
        if (eventRouter != null) {
            eventRouter.fireEvent(event);
        }
    }
}
