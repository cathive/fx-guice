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

package com.cathive.fx.guice.fxml;

import java.lang.reflect.Field;

import javax.inject.Inject;

import com.cathive.fx.guice.FXMLController;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

/**
 * 
 * @author Benjamin P. Jung
 */
final class FXMLControllerTypeListener implements TypeListener {

    @Inject private FXMLLoadingScope fxmlLoadingScope;

    FXMLControllerTypeListener() {
        super();
    }

    @Override
    public <T> void hear(TypeLiteral<T> typeLiteral, TypeEncounter<T> typeEncounter) {
        for (final Field field : typeLiteral.getRawType().getDeclaredFields()) {
            if (field.isAnnotationPresent(FXMLController.class)) {
                final FXMLController annotation = field.getAnnotation(FXMLController.class);
                typeEncounter.register(new FXMLControllerMembersInjector<T>(field, annotation, fxmlLoadingScope));
            }
        }
    }

}
