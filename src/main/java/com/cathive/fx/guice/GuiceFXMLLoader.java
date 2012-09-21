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
import java.util.ResourceBundle;

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

    /** Wrapped JavaFX FXMLLoader instance. */
    private final FXMLLoader loader = new FXMLLoader();

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

    /** Delegate to {@link FXMLLoader#getController()}. */ 
    @SuppressWarnings("unchecked")
    public <T> T getController() {
        return (T) loader.getController();
    }

    /** Delegate to {@link FXMLLoader#getLocation(). */
    public URL getLocation() {
        return loader.getLocation();
    }
    /** Delegate to {@link FXMLLoader#getResources(). */
    public ResourceBundle getResources() {
        return loader.getResources();
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
     *             The loaded object hierarchy
     * @throws IOException
     * @see javafx.fxml.FXMLLoader#load(URL, ResourceBundle)
     */
    @SuppressWarnings("unchecked")
    public <N> N load(final URL url, final ResourceBundle resources) throws IOException {

        return (N)loadWithController(url, resources).getNode();
    }
    
    public <N extends Node> FXMLResult<N> loadWithController(final URL url, final ResourceBundle resources) throws IOException  {

        fxmlLoadingScope.enter();

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

        @SuppressWarnings("unchecked")
        final N value = (N) loader.load(url.openStream());

        fxmlLoadingScope.exit();

        return new FXMLResult<N>(value, loader.getController());
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
     *             The loaded object hierarchy
     * @throws IOException
     * @see javafx.fxml.FXMLLoader#load(URL)
     */
    public <N> N load(final URL url) throws IOException {
        // Delegate method call
        return load(url, null);
    }

}
