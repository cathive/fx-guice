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
import com.cathive.fx.guice.GuiceFXMLLoader;
import com.google.inject.Injector;
import com.google.inject.MembersInjector;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;

/**
 * @param <T>
 * @author Benjamin P. Jung
 * @since 2.0.0
 */
final class FXMLComponentMembersInjector<T> implements MembersInjector<T> {

    private Injector injector;

    private final GuiceFXMLLoader fxmlLoader;

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = Logger.getLogger(FXMLComponentMembersInjector.class.getName());

    private final FXMLComponent annotation;

    FXMLComponentMembersInjector(final Injector injector, final GuiceFXMLLoader fxmlLoader, final FXMLComponent annotation) {
        super();
        this.injector = injector;
        this.fxmlLoader = fxmlLoader;
        this.annotation = annotation;
    }

    @Override
    public void injectMembers(final T instance) {

        String locationString = annotation.location();
        if (locationString.isEmpty()) {
            locationString = format("%s.fxml", instance.getClass().getSimpleName());
            LOGGER.log(Level.FINE, "No location for FXML component has been set for class '{0}'. Assuming default ('{1}').", new Object[] { instance.getClass().getName(), locationString });
        }
        URL location;
        location = instance.getClass().getResource(locationString);
        if (location == null) {
            LOGGER.log(Level.FINE, "Location '{0}' cannot be found on the classpath. Trying to construct a new URL...", locationString);
            try {
                location = new URL(locationString);
            } catch (final MalformedURLException e) {
                throw new RuntimeException(format("Cannot construct URL from string '%s'.", locationString), e);
            }
        }
        final FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        final String resourcesString = annotation.resources();
        if (!resourcesString.isEmpty()) {
            fxmlLoader.setResources(ResourceBundle.getBundle(resourcesString));
        }
        fxmlLoader.setCharset(Charset.forName(annotation.charset()));
        fxmlLoader.setControllerFactory(param -> {
            // Use our Guice injector to fetch an instance of the desired
            // controller class
            return param == null ? null : this.injector.getInstance(param);
        });
        fxmlLoader.setRoot(instance);

        // Invoke "fxmlLoader.setTemplate(true)" if we are using JavaFX 8.0 or
        // higher to improve performance on objects that are created multiple times.
        try {
            final Method setTemplateMethod = FXMLLoader.class.getMethod("setTemplate", boolean.class);
            setTemplateMethod.invoke(fxmlLoader, Boolean.TRUE);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            // We simply ignore this exception. It means that we are using
            // a JavaFX runtime prior to JavaFX 8.0.
        }

        // Actual instantiation of the component has to happen on the JavaFX thread.
        // We simply delegate the loading.
        Runnable loader = () -> {
            try {
                final Object loaded = fxmlLoader.load();
                if (loaded != instance) {
                    throw new IllegalStateException("Loading of FXML component went terribly wrong! :-(");
                }
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }

        };

        if (Platform.isFxApplicationThread()) {
            loader.run();
        } else {
            Platform.runLater(loader);
        }

    }

}
