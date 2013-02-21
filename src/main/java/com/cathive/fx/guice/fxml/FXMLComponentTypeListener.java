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

import com.cathive.fx.guice.FXMLComponent;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

/**
 * 
 * @author Benjamin P. Jung
 * @since 2.0.0
 */
final class FXMLComponentTypeListener implements TypeListener {

    FXMLComponentTypeListener() {
        super();
    }

    @Override
    public <T> void hear(TypeLiteral<T> typeLiteral, TypeEncounter<T> typeEncounter) {
        final Class<? super T> rawType = typeLiteral.getRawType();
        if (rawType.isAnnotationPresent(FXMLComponent.class)) {
            final FXMLComponent annotation = rawType.getAnnotation(FXMLComponent.class);
            final FXMLComponentMembersInjector<T> membersInjector = new FXMLComponentMembersInjector<T>(annotation);
            typeEncounter.register(membersInjector);
        }
    }

}
