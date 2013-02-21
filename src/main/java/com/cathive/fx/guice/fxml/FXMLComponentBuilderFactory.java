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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.cathive.fx.guice.FXMLComponentBuilder;
import com.google.inject.Injector;

import javafx.fxml.JavaFXBuilderFactory;
import javafx.util.Builder;
import javafx.util.BuilderFactory;

/**
 * 
 * @author Benjamin P. Jung
 * @since 2.0.0
 */
@Singleton
final class FXMLComponentBuilderFactory implements BuilderFactory {

    /** Logger for this class */
    private static final Logger LOGGER = Logger.getLogger(FXMLComponentBuilderFactory.class.getName());

    @Inject
    private Injector injector;

    private final Map<Class<?>, FXMLComponentBuilder<?>> builders = new HashMap<>();
    private final JavaFXBuilderFactory javaFxBuilderFactory = new JavaFXBuilderFactory();

    @Override
    public Builder<?> getBuilder(final Class<?> objectClass) {
        LOGGER.finest(String.format("Fetching builder for class '%s'.", objectClass.getName()));
        if (builders.containsKey(objectClass)) {
            final FXMLComponentBuilder<?> builder = builders.get(objectClass);
            LOGGER.finest(String.format("Builder for class '%s' found: '%s'.", objectClass.getName(), builder.getClass().getName()));
            return builder;
        }
        return javaFxBuilderFactory.getBuilder(objectClass);
    }

    public <T> void registerBuilder(final Class<? super T> objectClass, final Class<? extends FXMLComponentBuilder<?>> factoryClass) {
        if (builders.containsKey(objectClass)) {
            throw new IllegalStateException(String.format("Builder for class '%s' has already been registered.", objectClass.getName()));
        }
        builders.put(objectClass, injector.getInstance(factoryClass));
    }

}
