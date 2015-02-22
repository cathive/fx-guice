/*
 * Copyright (C) 2013-2015 The Cat Hive Developers.
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

import com.cathive.fx.guice.FXMLComponent;
import com.google.inject.Injector;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.util.Builder;
import javafx.util.BuilderFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @see FXMLComponentBuilder
 * @author Benjamin P. Jung
 * @since 2.1.0
 */
@Singleton
public final class FXMLComponentBuilderFactory implements BuilderFactory {

    @Inject private Injector injector;

    private static final Logger LOGGER = Logger.getLogger(FXMLComponentBuilderFactory.class.getName());
    private final JavaFXBuilderFactory defaultBuilderFactory = new JavaFXBuilderFactory();

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Builder<?> getBuilder(final Class<?> componentClass) {
        final String className = componentClass.getName();
        LOGGER.log(Level.FINE, "Searching builder for component class '{0}'.", className);
        if (componentClass.isAnnotationPresent(FXMLComponent.class)) {
            LOGGER.log(Level.FINE, "Creating FXMLComponentBuilder for class '{0}'.", className);
            return new FXMLComponentBuilder(injector, componentClass);
        }
        // Fall back to the default builder factory if we are not dealing with a FXMLComponent class.
        return defaultBuilderFactory.getBuilder(componentClass);
    }

}
