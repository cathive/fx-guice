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

package com.cathive.fx.guice.prefs;

import java.lang.reflect.Field;

import com.cathive.fx.guice.PersistentProperty;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

/**
 * 
 * @author Benjamin P. Jung
 * @author comtel2000
 */
class PersistentPropertyTypeListener implements TypeListener {

	@Override
	public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
		for (Field field : type.getRawType().getDeclaredFields()) {
			if (field.getType() == PersistentPropertyBinder.class && field.isAnnotationPresent(PersistentProperty.class)) {
				PersistentProperty annotation = field.getAnnotation(PersistentProperty.class);
				encounter.register(new PersistentPropertyMembersInjector<I>(field, annotation));
			}
		}

	}

}
