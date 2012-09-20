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
import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;

import javafx.application.Application;

import com.cathive.fx.guice.controllerlookup.FXMLLoadingModule;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * @author Benjamin P. Jung
 */
public abstract class GuiceApplication extends Application {

    /**
     * The Guice Injector instance that is being used within
     * the context of this application.
     * @see #createInjector()
     * @see #getInjector()
     */
    private Injector injector;

    private static final Set<Class<? extends Annotation>> injectAnnotationClasses = new HashSet<>();
    static {
    	injectAnnotationClasses.add(com.google.inject.Inject.class);
    	injectAnnotationClasses.add(javax.inject.Inject.class);
    }

    /**
     * To make sure that the initialization of Guice-based JavaFX application
     * works flawlessly, the original init method of the base JavaFX Application
     * class is overwritten here. All of the 
     */
    @Override
    public final void init() throws Exception {

        super.init();

        @SuppressWarnings("unchecked")
        final Class<GuiceApplication> clazz = (Class<GuiceApplication>) getClass();
        final GuiceApplication instance = this;
        
        Injector inj = createInjector();
        
        if (inj == null) {
            throw new IllegalStateException("Injector has not been created (yet)!");
        }
        
        inj = inj.createChildInjector(new FXMLLoadingModule());

        // Checks the GuiceApplication instance and makes sure that none of the constructors is
        // annotated with @Inject!
        for (final Constructor<?> c: instance.getClass().getConstructors()) {
            if (isInjectAnnotationPresent(c)) {
                throw new IllegalStateException("GuiceApplication with construtor that is marked with @Inject is not allowed!");
            }
        }
        

        // Inject all fields annotated with @Inject into this GuiceApplication instance.
        inj.injectMembers(instance);

        // Create a child injector and bind the instance of this GuiceApplication
        // to any instances of GuiceApplication.class.
        injector = inj.createChildInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(clazz).toInstance(instance);
            }
        });

    }

    /**
     * This method initializes the Guice Injector to be used for dependency
     * injection in the context of this application instance.
     * <p>
     * This method <strong>must not</strong> return <code>null</code>.
     * 
     * @param modules
     *            A mutable list of {@link Module} instances that should be
     *            loaded when creating the injector.
     * @return The injector to be used in context of this application.
     */
    public abstract Injector createInjector();

    /**
     * Returns the Google Guice Injector that is used within the context
     * of this JavaFX Application.
     * @return
     *     The Guice Injector that has been created during the initialization
     *     of this JavaFX Application.
     * @see #createInjector()
     */
    public final Injector getInjector() {
        return this.injector;
    }

    /**
     * Helper method to determine whether a given constructor is annotated with one of the Inject annotations
     * known by Guice.
     * @param constructor
     *     Constructor to be analyzed. Must not be <code>null</code>
     * @return
     *     <code>true</code> if the given constructor is annotated with an Inject annotation,
     *     <code>false<code> otherwise.
     * @see javax.inject.Inject
     * @see com.google.inject.Inject
     */
    private static final boolean isInjectAnnotationPresent(final Constructor<?> constructor) {
        for (final Class<? extends Annotation> annotationClass: injectAnnotationClasses) {
            if (constructor.isAnnotationPresent(annotationClass)) {
                // Directly returns if an @Inject annotation can be found.
                return true;
            }
        }
        return false;
    }

}
