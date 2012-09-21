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

import java.util.ArrayList;

import com.cathive.fx.guice.controllerlookup.ControllerLookup;
import com.cathive.fx.guice.controllerlookup.IdentifiableController;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

/**
 * Handles construction of objects annotated with {@link FXMLController}.
 * 
 * @author Andy Till
 *
 */
public class FXMLLoadingScope implements Scope {

    private ArrayList<IdentifiableController> identifiables;

    public FXMLLoadingScope() {
        super();
    }
    /**
     * Enter the scope. From here on in, controllers implementing
     * {@link IdentifiableController} and annotated wih {@link FXMLController} will
     * be retrievable from any {@link ControllerLookup} instance that is
     * injected.
     */
    public void enter() {
        identifiables = new ArrayList<IdentifiableController>();
    }

    /**
     * End the scope.
     */
    public void exit() {
        identifiables = null;
    }

    @Override
    public <T> Provider<T> scope(final Key<T> key, final Provider<T> unscoped) {
        return new Provider<T>() {
            @Override
            @SuppressWarnings("unchecked")
            public T get() {
                return (T) provideObject(key, unscoped);
            }
        };
    }

    protected <T> Object provideObject(Key<T> key, Provider<T> unscoped) {

        Object providedObject = unscoped.get();

        if (providedObject instanceof IdentifiableController) {
            if (identifiables != null) {
                identifiables.add((IdentifiableController) providedObject);
            }
        }

        return providedObject;
    }

    public ArrayList<IdentifiableController> getControllers() {
        return identifiables;
    }

    public boolean isInScope() {
        return (identifiables != null);
    }
}
