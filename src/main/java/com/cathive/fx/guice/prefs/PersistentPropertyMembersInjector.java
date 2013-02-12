/*
 * Copyright (C) 2013 The Cat Hive Developers.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cathive.fx.guice.prefs;

import static java.util.prefs.Preferences.systemNodeForPackage;
import static java.util.prefs.Preferences.userNodeForPackage;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.prefs.Preferences;

import javafx.beans.property.Property;
import javafx.beans.value.WritableBooleanValue;
import javafx.beans.value.WritableDoubleValue;
import javafx.beans.value.WritableFloatValue;
import javafx.beans.value.WritableIntegerValue;
import javafx.beans.value.WritableLongValue;
import javafx.beans.value.WritableStringValue;
import javafx.beans.value.WritableValue;

import com.cathive.fx.guice.prefs.PersistentProperty.NodeType;
import com.google.inject.MembersInjector;

/**
 * 
 * @author Benjamin P. Jung
 */
class PersistentPropertyMembersInjector<T> implements MembersInjector<T> {

    private final Field field;
    private final PersistentProperty annotation;

    PersistentPropertyMembersInjector(final Field field, final PersistentProperty annotation) {
        super();
        this.field = field;
        this.annotation = annotation;
        field.setAccessible(true);
    }

    @Override
    public void injectMembers(T instance) {

        final NodeType nodeType = annotation.type();
        final Class<?> nodeClass = annotation.clazz();

        final Preferences prefs;
        switch (nodeType) {
        case SYSTEM_NODE:
            prefs = systemNodeForPackage(nodeClass);
            break;
        case USER_NODE:
            prefs = userNodeForPackage(nodeClass);
            break;
        default:
            throw new IllegalStateException(String.format("Unknown Preferences node type: %s!", nodeType));
        }

        @SuppressWarnings("unchecked")
        final Class<? extends Property<?>> fieldType = (Class<? extends Property<?>>) field.getType();
        final String value = prefs.get(annotation.key(), null);
        if (value != null) {
            try {
                final Object fieldInstance = field.get(instance);
                final Method m = WritableValue.class.getMethod("setValue", Object.class);
                if (WritableStringValue.class.isAssignableFrom(fieldType)) {
                    m.invoke(fieldInstance, value);
                } else if (WritableBooleanValue.class.isAssignableFrom(fieldType)) {
                    m.invoke(fieldInstance, Boolean.valueOf(value));
                } else if (WritableIntegerValue.class.isAssignableFrom(fieldType)) {
                    m.invoke(fieldInstance, Integer.valueOf(value));
                } else if (WritableLongValue.class.isAssignableFrom(fieldType)) {
                    m.invoke(fieldInstance, Long.valueOf(value));
                } else if (WritableDoubleValue.class.isAssignableFrom(fieldType)) {
                    m.invoke(fieldInstance, Double.valueOf(value));
                } else if (WritableFloatValue.class.isAssignableFrom(fieldType)) {
                    m.invoke(fieldInstance, Float.valueOf(value));
                }
            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                // TODO Use a more meaningful exception
                throw new RuntimeException(e);
            }
        }

    }

}
