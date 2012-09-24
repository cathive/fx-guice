/*
 * Copyright (C) 2012 The Cat Hive Developers.
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

package com.cathive.fx.guice;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.util.Callback;

import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * If you want to `guicify` your JavaFX experience you can use an instance of
 * this class instead of the FXMLLoader that ships with JavaFX 2.x.
 * @see javafx.fxml.FXMLLoader
 * @author Benjamin P. Jung
 */
public final class GuiceFXMLLoader {

    /**
     * Guice Injector that will be used to fetch an instance of our `controller
     * class`.
     */
    private final Injector injector;

    private final FXMLLoadingScope fxmlLoadingScope;

    /**
     * This constructor is usually never called directly.
     * <p>Instead use an existing {@link com.google.inject.Injector} instance
     * to fetch an instance of this class.</p>
     * @param injector
     *              Usually injected via Guice.
     * @throws IllegalArgumentException
     *              if you try to pass a <code>null</code> value as
     *              injector instance.
     * @throws IllegalStateException
     *              if the injector has no binding for the {@link FXMLController}
     *              loading scope.
     */
    @Inject
    public GuiceFXMLLoader(final Injector injector) {
        super();
        if (injector == null) {
            throw new IllegalArgumentException("The Injector instance must not be null.");
        }
        if (!injector.getScopeBindings().containsKey(FXMLController.class)) {
            throw new IllegalStateException("FXMLController loading scope is not bound in your Injector.");
        }
        this.injector = injector;
        
        fxmlLoadingScope = injector.getInstance(FXMLLoadingScope.class);
    }

    /**
     * Loads an object hierarchy from a FXML document.
     * <p>A simple wrapper around the
     * {@link javafx.fxml.FXMLLoader#load(URL, ResourceBundle) load method}
     * of JavaFX' FXMLLoader class that adds a tiny notch of Guice-related
     * magic.</p>
     * 
     * @param url
     *             URL of the FXML resource to be loaded.
     * @param resources
     *             Resources to be used to localize strings.
     * @return
     *             The loaded object hierarchy encapsulated in a special result object.
     * @throws IOException
     * @see javafx.fxml.FXMLLoader#load(URL, ResourceBundle)
     */
    public Result load(final URL url, final ResourceBundle resources) throws IOException  {

        fxmlLoadingScope.enter(this);

        final FXMLLoader loader = new FXMLLoader();
        loader.setLocation(url);
        loader.setResources(resources);
        loader.setControllerFactory(new Callback<Class<?>, Object>() {
            @Override
            public Object call(Class<?> param) {
                // Use our Guice injector to fetch an instance of the desired
                // controller class
                return injector.getInstance(param);
            }
        });

        final Node value = (Node) loader.load(url.openStream());

        fxmlLoadingScope.exit();

        final Result result = new Result();
        result.location.set(loader.getLocation());
        result.controller.set(loader.getController());
        result.root.set(loader.getRoot());
        result.charset.set(loader.getCharset());
        return result;
    }

    /**
     * Loads an object hierarchy from a FXML document.
     * <p>A simple wrapper around the
     * {@link javafx.fxml.FXMLLoader#load(URL) load method}
     * of JavaFX' FXMLLoader class that adds a tiny notch of Guice-related
     * magic.</p>
     * 
     * @param url
     *             URL of the FXML resource to be loaded.
     * @return
     *             The loaded object hierarchy encapsulated in a special result object.
     * @throws IOException
     * @see javafx.fxml.FXMLLoader#load(URL)
     */
    public Result load(final URL url) throws IOException {
        // Delegate method call
        return load(url, null);
    }


    /**
     * A simple wrapper around the result of an FXML loading operation.
     * @author Benjamin P. Jung
     */
    public final static class Result {

        private ReadOnlyObjectWrapper<URL> location = new ReadOnlyObjectWrapper<>();
        public ReadOnlyObjectProperty<URL> locationProperty() { return this.location.getReadOnlyProperty(); }
        public URL getUrl() { return this.location.get(); }

        private ReadOnlyObjectWrapper<Object> root = new ReadOnlyObjectWrapper<>();
        public ReadOnlyObjectProperty<Object> rootProperty() { return this.root.getReadOnlyProperty(); }
        @SuppressWarnings("unchecked") public <N> N getRoot() { return (N) root.get(); }

        private ReadOnlyObjectWrapper<Object> controller = new ReadOnlyObjectWrapper<>();
        public ReadOnlyObjectProperty<Object> controllerProperty() { return this.controller.getReadOnlyProperty(); }
        @SuppressWarnings("unchecked") public <N> N getController() { return (N) this.controller.get(); }

        private ReadOnlyObjectWrapper<Charset> charset = new ReadOnlyObjectWrapper<>();
        public ReadOnlyObjectProperty<Charset> charsetProperty() { return this.charset.getReadOnlyProperty(); }
        public Charset getCharset() { return this.charset.get(); }

    }

}
