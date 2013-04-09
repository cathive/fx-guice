/*
 * Copyright (C) 2012-2013 The Cat Hive Developers.
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
import com.cathive.fx.guice.FXMLController;
import com.cathive.fx.guice.controllerlookup.ControllerLookup;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.matcher.Matchers;

/**
 * {@link Module} enabling interaction between multiple controllers loaded in
 * one control.
 * 
 * @author Andy Till
 * @author Benjamin P. Jung
 */
public final class FXMLLoadingModule extends AbstractModule {

    public FXMLLoadingModule() {
        super();
    }

    @Override
    protected void configure() {

        // FXMLLoadingScope
        final FXMLLoadingScope fxmlLoadingScope = new FXMLLoadingScope();
        bind(FXMLLoadingScope.class).toInstance(fxmlLoadingScope);
        bindScope(FXMLController.class, fxmlLoadingScope);
        bindScope(FXMLComponent.class, fxmlLoadingScope);

        // FXMLControllerTypeListener
        final FXMLControllerTypeListener fxmlControllerTypeListener = new FXMLControllerTypeListener();
        requestInjection(fxmlControllerTypeListener);
        bind(FXMLControllerTypeListener.class).toInstance(fxmlControllerTypeListener);
        bindListener(Matchers.any(), fxmlControllerTypeListener);

        // FXMLComponentTypeListener
        final FXMLComponentTypeListener fxmlComponentTypeListener = new FXMLComponentTypeListener();
        requestInjection(fxmlComponentTypeListener);
        bind(FXMLComponentTypeListener.class).toInstance(fxmlComponentTypeListener);
        bindListener(Matchers.any(), fxmlComponentTypeListener);

        // ControllerLookup
        bind(ControllerLookup.class).toProvider(new ControllerLookupProvider(fxmlLoadingScope));

    }


    private final class ControllerLookupProvider implements Provider<ControllerLookup> {
        private final FXMLLoadingScope fxmlLoadingScope;
        private ControllerLookupProvider(final FXMLLoadingScope fxmlLoadingScope) {
            super();
            this.fxmlLoadingScope = fxmlLoadingScope;
        }
        @Override
        public ControllerLookup get() {
            if (!fxmlLoadingScope.isActive()) {
                throw new IllegalStateException("A ControllerLookup instance cannot be injected while outside of the FXML Loading scope.");
            }
            return new ControllerLookup(fxmlLoadingScope.getIdentifiables());
        }
    }

}
