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

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ResourceBundle;

import javax.inject.Inject;

import javafx.fxml.FXMLLoader;

import com.cathive.fx.guice.FXMLComponent;
import com.cathive.fx.guice.FXMLComponentBuilder;
import com.google.inject.Injector;
import com.google.inject.MembersInjector;

/**
 * 
 * @author Benjamin P. Jung
 * 
 * @since 2.0.0
 *
 * @param <T>
 */
final class FXMLComponentMembersInjector<T> implements MembersInjector<T> {

    @Inject private Injector injector;
    @Inject private FXMLComponentBuilderFactory builderFactory;

    private final FXMLComponent annotation;

    FXMLComponentMembersInjector(final FXMLComponent annotation) {
        super();
        this.annotation = annotation;
    }

    @Override
    public void injectMembers(final T instance) {

        try {
            URL location;
            location = instance.getClass().getResource(annotation.location());
            if (location == null) {
                location = new URL(annotation.location());
            }
            final FXMLLoader fxmlLoader = new FXMLLoader(location);
            final String resourcesString = annotation.resources();
            if (!resourcesString.isEmpty()) {
                fxmlLoader.setResources(ResourceBundle.getBundle(resourcesString));
            }
            fxmlLoader.setCharset(Charset.forName(annotation.charset()));
            fxmlLoader.setController(instance);
            fxmlLoader.setRoot(instance);
            fxmlLoader.setBuilderFactory(builderFactory);
            final Object loaded = fxmlLoader.load();
            final Class<?> builderClass =  annotation.builderClass();
            if (builderClass != FXMLComponent.NopBuilder.class) {
                @SuppressWarnings("unchecked")
                final FXMLComponentBuilder<T> builderInstance = (FXMLComponentBuilder<T>) injector.getInstance(builderClass);
                builderInstance.applyTo(instance);
            }
            if (loaded != instance) {
                throw new IllegalStateException("Loading of FXML component went terribly wrong! :-(");
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

}
