/*
 * Copyright (C) 2015 comtel2000
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.annotation.PreDestroy;

import com.cathive.fx.guice.PersistentProperty.NodeType;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;

/**
 * This class allows applications to bind {@link Property} to
 * {@link Preferences} and store and retrieve it from the OS-specific registry.
 * The selected binding {@code key} is restricted to
 * {@link Preferences#MAX_KEY_LENGTH}
 * 
 * @author comtel2000
 *
 */
public class PersistentPropertyBinder {

	private final Preferences prefs;

	public PersistentPropertyBinder(Preferences p) {
		prefs = p;
	}

	/**
	 * Use the user preference tree with associated
	 * {@link PersistentPropertyBinder} package name
	 * 
	 * @see java.util.prefs.Preferences
	 */
	public PersistentPropertyBinder() {
		this(Preferences.userNodeForPackage(PersistentPropertyBinder.class));
	}

	/**
	 * Use the user preference tree with associated given class package name
	 * 
	 * @param c
	 *            package name
	 * 
	 * @see java.util.prefs.Preferences
	 */
	public PersistentPropertyBinder(Class<?> c) {
		this(Preferences.userNodeForPackage(c));
	}

	/**
	 * Use the selected preference tree with associated given class package name
	 * 
	 * @param c
	 *            package name
	 * @param pref
	 *            selected preference tree
	 * 
	 * @see com.cathive.fx.guice.PersistentProperty
	 */
	public PersistentPropertyBinder(Class<?> c, NodeType pref) {
		this(pref == NodeType.USER_NODE ? Preferences.userNodeForPackage(c) : Preferences.systemNodeForPackage(c));
	}

	@PreDestroy
	public void flush() {
		try {
			prefs.flush();
		} catch (BackingStoreException e) {
			throw new RuntimeException(e);
		}
	}

	public Preferences getPreferences() {
		return prefs;
	}

	/**
	 * Generates a bidirectional binding between the {@link Property} and the
	 * application store value. The store key is used by the property name
	 * {@link Property#getName()}
	 * 
	 * @param <T>
	 *            {@link Serializable} small object
	 * @param property
	 *            {@link Property} to bind
	 */
	public <T extends Serializable> void bind(ObjectProperty<T> property) {
		bind(property, property.getName());
	}

	/**
	 * Generates a bidirectional binding between the {@link Property} and the
	 * application store value identified by the {@code key} {@link String}.
	 * 
	 * @param <T>
	 *            {@link Serializable} small object
	 * @param property
	 *            {@link Property} to bind
	 * @param key
	 *            unique application store key
	 */
	@SuppressWarnings("unchecked")
	public <T extends Serializable> void bind(ObjectProperty<T> property, String key) {
		byte[] value = prefs.getByteArray(validateKey(key), null);
		if (value != null && value.length > 0) {
			try (ObjectInputStream stream = new ObjectInputStream(
					new ByteArrayInputStream(Base64.getDecoder().decode(value)))) {
				property.set((T) stream.readObject());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		property.addListener(o -> {
			T v = property.getValue();
			if (v == null) {
				prefs.putByteArray(key, new byte[] {});
				return;
			}
			try (ByteArrayOutputStream obj = new ByteArrayOutputStream()) {
				try (ObjectOutputStream stream = new ObjectOutputStream(obj)) {
					stream.writeObject(v);
					stream.flush();
				}
				prefs.putByteArray(key, Base64.getEncoder().encode(obj.toByteArray()));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}

	/**
	 * @see #bind(ObjectProperty)
	 * 
	 * @param property
	 *            {@link Property} to bind
	 */
	public void bind(BooleanProperty property) {
		bind(property, property.getName());
	}

	/**
	 * @see #bind(ObjectProperty, String)
	 * 
	 * @param property
	 *            {@link Property} to bind
	 * @param key
	 *            unique application store key
	 */
	public void bind(BooleanProperty property, String key) {
		if (prefs.get(validateKey(key), null) != null) {
			property.set(prefs.getBoolean(key, false));
		}
		property.addListener(o -> prefs.putBoolean(key, property.getValue()));
	}

	/**
	 * @see #bind(ObjectProperty)
	 * 
	 * @param property
	 *            {@link Property} to bind
	 */
	public void bind(IntegerProperty property) {
		bind(property, property.getName());
	}

	/**
	 * @see #bind(ObjectProperty, String)
	 * 
	 * @param property
	 *            {@link Property} to bind
	 * @param key
	 *            unique application store key
	 */
	public void bind(IntegerProperty property, String key) {
		if (prefs.get(validateKey(key), null) != null) {
			property.set(prefs.getInt(key, Integer.MIN_VALUE));
		}
		property.addListener(o -> prefs.putInt(key, property.getValue()));
	}

	/**
	 * @see #bind(ObjectProperty)
	 * 
	 * @param property
	 *            {@link Property} to bind
	 */
	public void bind(FloatProperty property) {
		bind(property, property.getName());
	}

	/**
	 * @see #bind(ObjectProperty, String)
	 * 
	 * @param property
	 *            {@link Property} to bind
	 * @param key
	 *            unique application store key
	 */
	public void bind(FloatProperty property, String key) {
		if (prefs.get(validateKey(key), null) != null) {
			property.set(prefs.getFloat(key, Float.NaN));
		}
		property.addListener(o -> prefs.putFloat(key, property.getValue()));
	}

	/**
	 * @see #bind(ObjectProperty)
	 * 
	 * @param property
	 *            {@link Property} to bind
	 */
	public void bind(DoubleProperty property) {
		bind(property, property.getName());
	}

	/**
	 * @see #bind(ObjectProperty, String)
	 * 
	 * @param property
	 *            {@link Property} to bind
	 * @param key
	 *            unique application store key
	 */
	public void bind(DoubleProperty property, String key) {
		if (prefs.get(validateKey(key), null) != null) {
			property.set(prefs.getDouble(key, Double.NaN));
		}
		property.addListener(o -> prefs.putDouble(key, property.getValue()));
	}

	/**
	 * @see #bind(ObjectProperty)
	 * 
	 * @param property
	 *            {@link Property} to bind
	 */
	public void bind(LongProperty property) {
		bind(property, property.getName());
	}

	/**
	 * @see #bind(ObjectProperty, String)
	 * 
	 * @param property
	 *            {@link Property} to bind
	 * @param key
	 *            unique application store key
	 */
	public void bind(LongProperty property, String key) {
		if (prefs.get(validateKey(key), null) != null) {
			property.set(prefs.getLong(key, Long.MIN_VALUE));
		}
		property.addListener(o -> prefs.putLong(key, property.getValue()));
	}

	/**
	 * @see #bind(ObjectProperty)
	 * 
	 * @param property
	 *            {@link Property} to bind
	 */
	public void bind(StringProperty property) {
		bind(property, property.getName());
	}

	/**
	 * @see #bind(ObjectProperty, String)
	 * 
	 * @param property
	 *            {@link Property} to bind
	 * @param key
	 *            unique application store key
	 */
	public void bind(final StringProperty property, String key) {
		String value = prefs.get(validateKey(key), null);
		if (value != null) {
			property.set(value);
		}
		property.addListener(o -> prefs.put(key, property.getValue()));
	}

	private final static String validateKey(String key) {
		if (key == null || key.length() == 0 || key.length() > Preferences.MAX_KEY_LENGTH) {
			throw new IllegalArgumentException("invalid binding key: " + String.valueOf(key));
		}
		return key;
	}
}
