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

package com.cathive.fx.guice.fxml;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.cathive.fx.guice.FXMLComponent;
import com.google.inject.Injector;

import javafx.util.Builder;
import javafx.util.StringConverter;
import javafx.util.converter.BigDecimalStringConverter;
import javafx.util.converter.BigIntegerStringConverter;
import javafx.util.converter.BooleanStringConverter;
import javafx.util.converter.ByteStringConverter;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LongStringConverter;
import javafx.util.converter.ShortStringConverter;

/**
 * Special builder class for components annotated with {@link FXMLComponent @FXMLComponent}.
 * @see FXMLComponentBuilderFactory
 * @author Benjamin P. Jung
 * @since 2.1.0
 */
final class FXMLComponentBuilder<T> extends AbstractMap<String, Object> implements Builder<T> {

    // FIXME The whole logic that finds appropriate setter methods and look ups the
    //       StringConverters in a Map seems a duplicate of what has already been implemented
    //       in the JavaFX runtime. Unfortunately I couldn't think of any better
    //       solution... :-(
    
    private static final Map<Class<?>, Class<? extends StringConverter<?>>> STRING_CONVERTERS;
    static {

        STRING_CONVERTERS = new HashMap<>();

        // String
        STRING_CONVERTERS.put(String.class, DefaultStringConverter.class);

        // Primitives
        STRING_CONVERTERS.put(boolean.class, BooleanStringConverter.class);
        STRING_CONVERTERS.put(byte.class, ByteStringConverter.class);
        STRING_CONVERTERS.put(double.class, DoubleStringConverter.class);
        STRING_CONVERTERS.put(float.class, FloatStringConverter.class);
        STRING_CONVERTERS.put(int.class, IntegerStringConverter.class);
        STRING_CONVERTERS.put(long.class, LongStringConverter.class);
        STRING_CONVERTERS.put(short.class, ShortStringConverter.class);

        // Primitive wrappers
        STRING_CONVERTERS.put(Boolean.class, BooleanStringConverter.class);
        STRING_CONVERTERS.put(Byte.class, ByteStringConverter.class);
        STRING_CONVERTERS.put(Double.class, DoubleStringConverter.class);
        STRING_CONVERTERS.put(Float.class, FloatStringConverter.class);
        STRING_CONVERTERS.put(Integer.class, IntegerStringConverter.class);
        STRING_CONVERTERS.put(Long.class, LongStringConverter.class);
        STRING_CONVERTERS.put(Short.class, ShortStringConverter.class);

        // Other types
        STRING_CONVERTERS.put(BigDecimal.class, BigDecimalStringConverter.class);
        STRING_CONVERTERS.put(BigInteger.class, BigIntegerStringConverter.class);
    }

    private final Injector injector;
    private final Class<T> componentClass;

    private final Map<String, Object> componentProperties = new HashMap<>();

    FXMLComponentBuilder(final Injector injector, final Class<T> componentClass) {
        super();
        this.injector = injector;
        this.componentClass = componentClass;
    }

    @Override
    public T build() {
        final T component = injector.getInstance(componentClass);
        for (String key: componentProperties.keySet()) {
            final Object value = componentProperties.get(key);
            final String setterName = String.format("set%s%s", key.substring(0, 1).toUpperCase(), key.substring(1));
            try {
                Method setterMethod = null;
                for (Method method: componentClass.getMethods()) {
                    if (method.getName().equals(setterName)) {
                        setterMethod = method;
                        break;
                    }
                }
                if (setterMethod == null) {
                    throw new IllegalStateException(String.format("No setter for field '%s' could be found.", key));
                }
                final StringConverter<?> stringConverter = getStringConverter(setterMethod.getParameterTypes()[0]);
                setterMethod.invoke(component, stringConverter.fromString((String) value));
            } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
                throw new RuntimeException(e);
            }
        }
        return component;
    }

    private StringConverter<?> getStringConverter(Class<?> valueClass) throws InstantiationException, IllegalAccessException {

        if (STRING_CONVERTERS.containsKey(valueClass)) {
            return STRING_CONVERTERS.get(valueClass).newInstance();
        } else {
            throw new IllegalArgumentException(String.format("Can't find StringConverter for class '%s'.", valueClass.getName()));
        }

    }

    @Override
    public Object put(String key, Object value) {
        componentProperties.put(key, value);
        return null;
    }

    @Override
    public Set<Map.Entry<String, Object>> entrySet() {
        throw new UnsupportedOperationException();
    }

}
