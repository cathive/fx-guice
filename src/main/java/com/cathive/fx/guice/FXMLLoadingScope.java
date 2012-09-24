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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
class FXMLLoadingScope implements Scope {

    private GuiceFXMLLoader fxmlLoader;

    private Set<IdentifiableController> identifiables;

    public FXMLLoadingScope() {
        super();
    }
    /**
     * Enter the scope. From here on in, controllers implementing
     * {@link IdentifiableController} and annotated wih {@link FXMLController} will
     * be retrievable from any {@link ControllerLookup} instance that is
     * injected.
     */
    public void enter(final GuiceFXMLLoader fxmlLoader) {
        this.fxmlLoader = fxmlLoader;
        this.identifiables = new HashSet<>();
    }

    /**
     * End the scope.
     */
    public void exit() {
        this.identifiables = null;
        this.fxmlLoader = null;
    }

    @Override
    public <T> Provider<T> scope(final Key<T> key, final Provider<T> unscoped) {
        return new Provider<T>() {
            @Override
            public T get() {
                final T providedObject = unscoped.get();
                if (identifiables != null && providedObject instanceof IdentifiableController) {
                    final IdentifiableController identifiable = (IdentifiableController) providedObject;
                    identifiables.add(identifiable);
                }
                return providedObject;
            }
        };
    }

	public <T> T getInstance(final String controllerId) {
        for (final IdentifiableController identifiable: identifiables) {
            if (identifiable.getId().equals(controllerId)) {
                @SuppressWarnings("unchecked")
                T controllerInstance = (T) identifiable;
                return controllerInstance;
            }
        }
        // TODO Throw an exception maybe?
        return null;
    }

    public Collection<IdentifiableController> getIdentifiables() {
        return identifiables;
    }

    /**
     * Checks whether the FXML Loading Scope is currently being used.
     * @return <code>true</code> if this scope is currently active, <code>false</code> otherwise.
     */
    public boolean isActive() {
        return (identifiables != null);
    }

}
