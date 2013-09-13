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

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.application.Application;

import com.cathive.fx.guice.fxml.FXMLLoadingModule;
import com.cathive.fx.guice.prefs.PersistentPropertyModule;
import com.cathive.fx.guice.thread.FxApplicationThreadModule;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * A simple replacement for the default JavaFX Application class that utilizes
 * Google Guice for dependency injection.
 * 
 * @author Benjamin P. Jung
 */
public abstract class GuiceApplication extends Application {

    /**
     * The Guice Injector instance that is being used within
     * the context of this application.
     * @see #init(List)
     * @see #getInjector()
     */
    private Injector injector;

    /**
     * List of annotations that are not allowed on constructors of
     * {@code GuiceApplication} instances.
     */
    private static final Set<Class<? extends Annotation>> injectAnnotationClasses = new HashSet<>();
    static {
        injectAnnotationClasses.add(com.google.inject.Inject.class);
        injectAnnotationClasses.add(javax.inject.Inject.class);
    }

    /**
     * To make sure that the initialization of Guice-based JavaFX application
     * works flawlessly, the original init method of the base JavaFX Application
     * class is overwritten here.
     * <p>This class offers it's own method for custom initialization, which will be called
     * during the invocation of this overriden method: {@link #init(java.util.List)}.</p>
     */
    @Override
    public final void init() throws Exception {

        super.init();

        @SuppressWarnings("unchecked")
        final Class<GuiceApplication> clazz = (Class<GuiceApplication>) getClass();
        final GuiceApplication instance = this;

        // Checks the GuiceApplication instance and makes sure that none of the constructors is
        // annotated with @Inject!
        for (final Constructor<?> c: instance.getClass().getConstructors()) {
            if (isInjectAnnotationPresent(c)) {
                throw new IllegalStateException("GuiceApplication with constructor that is marked with @Inject is not allowed!");
            }
        }

        final Set<Module> modules = new HashSet<>();
        modules.add(new AbstractModule() {
            @Override
            protected void configure() {
                bind(clazz).toInstance(instance);
            }
        });
        modules.add(new FXMLLoadingModule());
        modules.add(new FxApplicationThreadModule());
        modules.add(new PersistentPropertyModule());

        // Propagates initialization of additional modules to the specific
        // subclass of this GuiceApplication instance.
        final List<Module> additionalModules = new ArrayList<>();
        init(additionalModules);
        modules.addAll(additionalModules);

        // Creates an injector with all of the required modules.
        injector = Guice.createInjector(modules);

        // Injects all fields annotated with @Inject into this GuiceApplication instance.
        injector.injectMembers(instance);

    }

    /**
     * This method is used to fetch and/or create (Guice) modules necessary
     * to fully construct this application.
     * <p>The modules that are initialized in this method and added to the
     * passed List will be used to create the {@link Injector} instance that is
     * used in the context of this application.</p>
     * @param modules
     *   A list of modules (initially empty) that shall be used to create the
     *   injector to be used in the context of this application.
     * 
     * @see #getInjector()
     * 
     * @throws Exception
     *   if anything goes wrong during initialization.
     */
    public abstract void init(List<Module> modules) throws Exception;

    /**
     * Returns the Google Guice Injector that is used within the context
     * of this JavaFX Application.
     * @return
     *     The Guice Injector that has been created during the initialization
     *     of this JavaFX Application.
     * @see #init(List)
     */
    public final Injector getInjector() {
        return this.injector;
    }

    /**
     * Helper method to determine whether a given {@link AccessibleObject} is annotated
     * with one of the Inject annotations known by Guice.
     * 
     * @param object
     *     Accessible object to be analyzed. Must not be {@code null}
     * @return
     *     {@code true} if the given constructor is annotated with an Inject annotation,
     *     {@code false} otherwise.
     * 
     * @see javax.inject.Inject
     * @see com.google.inject.Inject
     */
    private static boolean isInjectAnnotationPresent(final AccessibleObject object) {
        boolean found = false;
        for (final Class<? extends Annotation> annotationClass: injectAnnotationClasses) {
            if (object.isAnnotationPresent(annotationClass)) {
                found = true;
                break;
            }
        }
        return found;
    }

}
