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

package com.cathive.fx.guice;

import java.util.logging.Logger;

import javax.inject.Inject;

import com.google.inject.Injector;

import javafx.util.Builder;

/**
 * Abstract superclass for builders that can be used in conjunction
 * with instances of classes annotated with {@link FXMLComponent}.
 * 
 * @see FXMLComponent#builderClass()
 * @author Benjamin P. Jung
 * @since 2.0.0
 */
public abstract class FXMLComponentBuilder<T> implements Builder<T> {

    /** Logger for this class */
    private static final Logger LOGGER = Logger.getLogger(FXMLComponentBuilder.class.getName());

    /** The class of the component to be instantiated via Guice. */
    private final Class<T> componentClass;

    /** Our brave injector that handles instantiation of all classes. */
    @Inject private Injector injector;

    /**
     * Creates a new component builder.
     * @param componentClass
     *     Class that will be used to create new instances of &lt;T&gt;.
     */
    protected FXMLComponentBuilder(final Class<T> componentClass) {
        super();
        this.componentClass = componentClass;
    }


    /**
     * This method must not be overriden, since creation of all instances is done via Guice.
     * <p>Implement {@link FXMLComponentBuilder#applyTo(Object)} instead.
     */
    @Override
    public final T build() {
        if (injector == null) {
            throw new IllegalStateException("Guice Injector is 'null'. Probably you instantiated this FXMLComponentBuilder directly instead on using a Guice Injector!");
        }
        LOGGER.finest(String.format("Building component of type '%s'.", componentClass.getName()));
        final T instance = injector.getInstance(componentClass);
        applyTo(instance);
        return instance;
    }

    /**
     * TODO Javadoc
     * @param component
     *     The compoment that shall be updated.
     */
    public abstract void applyTo(T component);

}
