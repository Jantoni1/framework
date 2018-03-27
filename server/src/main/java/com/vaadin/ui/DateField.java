/*
 * Copyright 2000-2018 Vaadin Ltd.
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
package com.vaadin.ui;

import java.time.LocalDate;

import com.vaadin.shared.ui.datefield.LocalDateFieldState;

/**
 * A date entry component, which displays the actual date selector as a popup.
 *
 * @see AbstractLocalDateField
 * @see InlineDateField
 * @author Vaadin Ltd.
 * @since 8.0
 */
public class DateField extends AbstractLocalDateField {

    /**
     * Constructs an empty <code>DateField</code> with no caption.
     */
    public DateField() {
        super();
    }

    /**
     * Constructs a new <code>DateField</code> with the given caption and
     * initial text contents.
     *
     * @param caption
     *            the caption <code>String</code> for the editor.
     * @param value
     *            the LocalDate value.
     */
    public DateField(String caption, LocalDate value) {
        super(caption, value);
    }

    /**
     * Constructs an empty <code>DateField</code> with caption.
     *
     * @param caption
     *            the caption of the datefield.
     */
    public DateField(String caption) {
        super(caption);
    }

    /**
     * Constructs a new {@code DateField} with a value change listener.
     * <p>
     * The listener is called when the value of this {@code DateField} is
     * changed either by the user or programmatically.
     *
     * @param valueChangeListener
     *            the value change listener, not {@code null}
     */
    public DateField(ValueChangeListener<LocalDate> valueChangeListener) {
        super();
        addValueChangeListener(valueChangeListener);
    }

    /**
     * Constructs a new {@code DateField} with the given caption and a value
     * change listener.
     * <p>
     * The listener is called when the value of this {@code DateField} is
     * changed either by the user or programmatically.
     *
     * @param caption
     *            the caption for the field
     * @param valueChangeListener
     *            the value change listener, not {@code null}
     */
    public DateField(String caption,
            ValueChangeListener<LocalDate> valueChangeListener) {
        this(valueChangeListener);
        setCaption(caption);
    }

    /**
     * Constructs a new {@code DateField} with the given caption, initial
     * text contents and a value change listener.
     * <p>
     * The listener is called when the value of this {@code DateField} is
     * changed either by the user or programmatically.
     *
     * @param caption
     *            the caption for the field
     * @param value
     *            the value for the field, not {@code null}
     * @param valueChangeListener
     *            the value change listener, not {@code null}
     */
    public DateField(String caption, LocalDate value,
            ValueChangeListener<LocalDate> valueChangeListener) {
        this(caption, value);
        addValueChangeListener(valueChangeListener);
    }

    /**
     * Returns the current placeholder text.
     *
     * @see #setPlaceholder(String)
     * @return the placeholder text
     */
    public String getPlaceholder() {
        return getState(false).placeholder;
    }

    /**
     * Sets the placeholder text. The placeholder is text that is displayed when
     * the field would otherwise be empty, to prompt the user for input.
     *
     * @param placeholder
     *            the placeholder text to set
     */
    public void setPlaceholder(String placeholder) {
        getState().placeholder = placeholder;
    }

    @Override
    protected LocalDateFieldState getState() {
        return (LocalDateFieldState) super.getState();
    }

    @Override
    protected LocalDateFieldState getState(boolean markAsDirty) {
        return (LocalDateFieldState) super.getState(markAsDirty);
    }

    /**
     * Checks whether the text field is enabled (default) or not.
     *
     * @see #setTextFieldEnabled(boolean)
     *
     * @return <b>true</b> if the text field is enabled, <b>false</b> otherwise.
     */
    public boolean isTextFieldEnabled() {
        return getState(false).textFieldEnabled;
    }

    /**
     * Enables or disables the text field. By default the text field is enabled.
     * Disabling it causes only the button for date selection to be active, thus
     * preventing the user from entering invalid dates.
     *
     * See <a href="http://dev.vaadin.com/ticket/6790">issue 6790</a>.
     *
     * @param state
     *            <b>true</b> to enable text field, <b>false</b> to disable it.
     */
    public void setTextFieldEnabled(boolean state) {
        getState().textFieldEnabled = state;
    }

    /**
     * Set a description that explains the usage of the Widget for users of
     * assistive devices.
     *
     * @param description
     *            String with the description
     */
    public void setAssistiveText(String description) {
        getState().descriptionForAssistiveDevices = description;
    }

    /**
     * Get the description that explains the usage of the Widget for users of
     * assistive devices.
     *
     * @return String with the description
     */
    public String getAssistiveText() {
        return getState(false).descriptionForAssistiveDevices;
    }
}
