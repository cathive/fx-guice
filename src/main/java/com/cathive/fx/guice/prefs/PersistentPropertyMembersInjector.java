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
import java.util.Objects;

import javax.inject.Inject;

import com.cathive.fx.guice.PersistentProperty;
import com.cathive.fx.guice.PersistentProperty.NodeType;
import com.google.inject.Injector;
import com.google.inject.MembersInjector;

/**
 * 
 * @author Benjamin P. Jung
 * @author comtel2000
 *
 * @param <T>
 */
class PersistentPropertyMembersInjector<T> implements MembersInjector<T> {

	@Inject
	Injector injector;
	
	private final Field field;
	private final PersistentProperty annotation;

	PersistentPropertyMembersInjector(Field field, PersistentProperty annotation) {
		this.field = field;
		this.annotation = annotation;
		field.setAccessible(true);
		
	}

	@Override
	public void injectMembers(T t) {

		NodeType node = Objects.requireNonNull(annotation.type());
		Class<?> clazz = annotation.clazz();
		try {
			field.set(t, new PersistentPropertyBinder(clazz != null ? clazz : field.getDeclaringClass(), node));
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
