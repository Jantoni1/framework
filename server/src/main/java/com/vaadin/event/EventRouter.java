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

package com.vaadin.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.server.ErrorEvent;
import com.vaadin.server.ErrorHandler;
import com.vaadin.shared.Registration;
import com.vaadin.shared.communication.SharedState;
import com.vaadin.shared.ui.ComponentStateUtil;

/**
 * <code>EventRouter</code> class implementing the inheritable event listening
 * model. For more information on the event model see the
 * {@link com.vaadin.event package documentation}.
 *
 * @author Vaadin Ltd.
 * @since 3.0
 */
@SuppressWarnings("serial")
public class EventRouter implements MethodEventSource {

    /**
     * List of registered listeners.
     */
    private LinkedHashSet<ListenerMethod> listenerList = null;

    /*
     * Registers a new listener with the specified activation method to listen
     * events generated by this component. Don't add a JavaDoc comment here, we
     * use the default documentation from implemented interface.
     */
    @Deprecated
    @Override
    public Registration addListener(Class<?> eventType, Object object,
            Method method) {
        Objects.requireNonNull(object, "Listener must not be null.");
        getLogger().log(Level.WARNING, "Adding listeners with type Object is"
                + " deprecated, event listener should extend SerializableEventListener");
        if (listenerList == null) {
            listenerList = new LinkedHashSet<>();
        }
        ListenerMethod listenerMethod = new ListenerMethod(eventType, object,
                method);
        listenerList.add(listenerMethod);
        return () -> listenerList.remove(listenerMethod);
    }

    /*
     * Registers a new listener with the specified activation method to listen
     * events generated by this component. Don't add a JavaDoc comment here, we
     * use the default documentation from implemented interface.
     */
    @Override
    public Registration addListener(Class<?> eventType,
            SerializableEventListener listener, Method method) {
        Objects.requireNonNull(listener, "Listener must not be null.");
        if (listenerList == null) {
            listenerList = new LinkedHashSet<>();
        }
        ListenerMethod listenerMethod = new ListenerMethod(eventType, listener,
                method);
        listenerList.add(listenerMethod);
        return () -> listenerList.remove(listenerMethod);
    }

    /**
     * Registers a new event listener with the specified activation method to
     * listen events generated by this component. If the activation method does
     * not have any arguments the event object will not be passed to it when
     * it's called.
     *
     * <p>
     * This method additionally informs the event-api to stop routing events
     * with the given {@code eventIdentifier} to the components handleEvent
     * function call.
     * </p>
     *
     * <p>
     * The only way to remove the listener is to use the returned
     * {@link Registration}. The other methods, e.g.
     * {@link #removeAllListeners()} do not do that.
     * </p>
     *
     * <p>
     * For more information on the inheritable event mechanism see the
     * {@link com.vaadin.event com.vaadin.event package documentation}.
     * </p>
     * 
     * @deprecated As of 8.12. Use
     *             {@link #addListener(Class, SerializableEventListener, Method, String, SharedState)}
     *             instead
     * 
     * @param eventType
     *            the type of the listened event. Events of this type or its
     *            subclasses activate the listener.
     * @param target
     *            the object instance who owns the activation method.
     * @param method
     *            the activation method.
     * @param eventIdentifier
     *            the identifier of the event to listen for
     * @param state
     *            The component State
     * @return a registration object for removing the listener
     * @throws IllegalArgumentException
     *             unless {@code method} has exactly one match in {@code target}
     * @throws NullPointerException
     *             if {@code target} is {@code null}
     * @since 8.2
     */
    @Deprecated
    public Registration addListener(Class<?> eventType, Object target,
            Method method, String eventIdentifier, SharedState state) {
        getLogger().log(Level.WARNING, "Adding listeners with type Object is"
                + " deprecated, event listener should extend SerializableEventListener");
        if (listenerList == null) {
            listenerList = new LinkedHashSet<>();
        }
        ListenerMethod listenerMethod = new ListenerMethod(eventType, target,
                method);
        listenerList.add(listenerMethod);

        Registration registration = ComponentStateUtil
                .addRegisteredEventListener(state, eventIdentifier);

        return () -> {
            listenerList.remove(listenerMethod);
            if (!hasListeners(eventType)) {
                registration.remove();
            }
        };
    }

    /**
     * Registers a new event listener with the specified activation method to
     * listen events generated by this component. If the activation method does
     * not have any arguments the event object will not be passed to it when
     * it's called.
     *
     * <p>
     * This method additionally informs the event-api to stop routing events
     * with the given {@code eventIdentifier} to the components handleEvent
     * function call.
     * </p>
     *
     * <p>
     * The only way to remove the listener is to use the returned
     * {@link Registration}. The other methods, e.g.
     * {@link #removeAllListeners()} do not do that.
     * </p>
     *
     * <p>
     * For more information on the inheritable event mechanism see the
     * {@link com.vaadin.event com.vaadin.event package documentation}.
     * </p>
     *
     * @param eventType
     *            the type of the listened event. Events of this type or its
     *            subclasses activate the listener.
     * @param listener
     *            the listener instance who owns the activation method.
     * @param method
     *            the activation method.
     * @param eventIdentifier
     *            the identifier of the event to listen for
     * @param state
     *            The component State
     * @return a registration object for removing the listener
     * @throws IllegalArgumentException
     *             unless {@code method} has exactly one match in {@code target}
     * @throws NullPointerException
     *             if {@code target} is {@code null}
     * @since 8.12
     */
    public Registration addListener(Class<?> eventType,
            SerializableEventListener listener, Method method,
            String eventIdentifier, SharedState state) {
        if (listenerList == null) {
            listenerList = new LinkedHashSet<>();
        }
        ListenerMethod listenerMethod = new ListenerMethod(eventType, listener,
                method);
        listenerList.add(listenerMethod);

        Registration registration = ComponentStateUtil
                .addRegisteredEventListener(state, eventIdentifier);

        return () -> {
            listenerList.remove(listenerMethod);
            if (!hasListeners(eventType)) {
                registration.remove();
            }
        };
    }

    /*
     * Registers a new listener with the specified named activation method to
     * listen events generated by this component. Don't add a JavaDoc comment
     * here, we use the default documentation from implemented interface.
     */
    @Deprecated
    @Override
    public Registration addListener(Class<?> eventType, Object object,
            String methodName) {
        Objects.requireNonNull(object, "Listener must not be null.");
        getLogger().log(Level.WARNING, "Adding listeners with type Object is"
                + " deprecated, event listener should extend SerializableEventListener");
        if (listenerList == null) {
            listenerList = new LinkedHashSet<>();
        }
        ListenerMethod listenerMethod = new ListenerMethod(eventType, object,
                methodName);
        listenerList.add(listenerMethod);
        return () -> listenerList.remove(listenerMethod);
    }

    /*
     * Registers a new listener with the specified named activation method to
     * listen events generated by this component. Don't add a JavaDoc comment
     * here, we use the default documentation from implemented interface.
     */
    @Override
    public Registration addListener(Class<?> eventType,
            SerializableEventListener listener, String methodName) {
        Objects.requireNonNull(listener, "Listener must not be null.");
        if (listenerList == null) {
            listenerList = new LinkedHashSet<>();
        }
        ListenerMethod listenerMethod = new ListenerMethod(eventType, listener,
                methodName);
        listenerList.add(listenerMethod);
        return () -> listenerList.remove(listenerMethod);
    }

    /*
     * Removes all registered listeners matching the given parameters. Don't add
     * a JavaDoc comment here, we use the default documentation from implemented
     * interface.
     */
    @Override
    @Deprecated
    public void removeListener(Class<?> eventType, Object target) {
        if (listenerList != null) {
            final Iterator<ListenerMethod> i = listenerList.iterator();
            while (i.hasNext()) {
                final ListenerMethod lm = i.next();
                if (lm.matches(eventType, target)) {
                    i.remove();
                    return;
                }
            }
        }
    }

    /*
     * Removes all registered listeners matching the given parameters. Don't add
     * a JavaDoc comment here, we use the default documentation from implemented
     * interface.
     */
    @Override
    public void removeListener(Class<?> eventType,
            SerializableEventListener listener) {
        removeListener(eventType, (Object) listener);
    }

    /*
     * Removes the event listener methods matching the given given parameters.
     * Don't add a JavaDoc comment here, we use the default documentation from
     * implemented interface.
     */
    @Override
    @Deprecated
    public void removeListener(Class<?> eventType, Object target,
            Method method) {
        if (listenerList != null) {
            final Iterator<ListenerMethod> i = listenerList.iterator();
            while (i.hasNext()) {
                final ListenerMethod lm = i.next();
                if (lm.matches(eventType, target, method)) {
                    i.remove();
                    return;
                }
            }
        }
    }

    /*
     * Removes the event listener method matching the given given parameters.
     * Don't add a JavaDoc comment here, we use the default documentation from
     * implemented interface.
     */
    @Override
    @Deprecated
    public void removeListener(Class<?> eventType, Object target,
            String methodName) {

        // Find the correct method
        final Method[] methods = target.getClass().getMethods();
        Method method = null;
        for (Method m : methods) {
            if (m.getName().equals(methodName)) {
                method = m;
                break;
            }
        }
        if (method == null) {
            throw new IllegalArgumentException();
        }

        // Remove the listeners
        if (listenerList != null) {
            final Iterator<ListenerMethod> i = listenerList.iterator();
            while (i.hasNext()) {
                final ListenerMethod lm = i.next();
                if (lm.matches(eventType, target, method)) {
                    i.remove();
                    return;
                }
            }
        }
    }

    /**
     * Removes all listeners from event router.
     */
    public void removeAllListeners() {
        listenerList = null;
    }

    /**
     * Sends an event to all registered listeners. The listeners will decide if
     * the activation method should be called or not.
     *
     * @param event
     *            the Event to be sent to all listeners.
     */
    public void fireEvent(EventObject event) {
        fireEvent(event, null);
    }

    /**
     * Sends an event to all registered listeners. The listeners will decide if
     * the activation method should be called or not.
     * <p>
     * If an error handler is set, the processing of other listeners will
     * continue after the error handler method call unless the error handler
     * itself throws an exception.
     *
     * @param event
     *            the Event to be sent to all listeners.
     * @param errorHandler
     *            error handler to use to handle any exceptions thrown by
     *            listeners or null to let the exception propagate to the
     *            caller, preventing further listener calls
     */
    public void fireEvent(EventObject event, ErrorHandler errorHandler) {
        // It is not necessary to send any events if there are no listeners
        if (listenerList != null) {

            // Make a copy of the listener list to allow listeners to be added
            // inside listener methods. Fixes #3605.

            // Send the event to all listeners. The listeners themselves
            // will filter out unwanted events.
            for (Object l : listenerList.toArray()) {
                ListenerMethod listenerMethod = (ListenerMethod) l;
                if (null != errorHandler) {
                    try {
                        listenerMethod.receiveEvent(event);
                    } catch (Exception e) {
                        errorHandler.error(new ErrorEvent(e));
                    }
                } else {
                    listenerMethod.receiveEvent(event);
                }
            }
        }
    }

    /**
     * Checks if the given Event type is listened by a listener registered to
     * this router.
     *
     * @param eventType
     *            the event type to be checked
     * @return true if a listener is registered for the given event type
     */
    public boolean hasListeners(Class<?> eventType) {
        if (listenerList != null) {
            for (ListenerMethod lm : listenerList) {
                if (lm.isType(eventType)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns all listeners that match or extend the given event type.
     *
     * @param eventType
     *            The type of event to return listeners for.
     * @return A collection with all registered listeners. Empty if no listeners
     *         are found.
     */
    public Collection<?> getListeners(Class<?> eventType) {
        List<Object> listeners = new ArrayList<>();
        if (listenerList != null) {
            for (ListenerMethod lm : listenerList) {
                if (lm.isOrExtendsType(eventType)) {
                    listeners.add(lm.getTarget());
                }
            }
        }
        return listeners;
    }

    private static final Logger getLogger() {
        return Logger.getLogger(EventRouter.class.getName());
    }
}
