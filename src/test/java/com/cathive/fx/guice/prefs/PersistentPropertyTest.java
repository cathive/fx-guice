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

import static org.testng.Assert.*;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.cathive.fx.guice.PersistentProperty;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * 
 * @author Benjamin P. Jung
 */
@Test
public class PersistentPropertyTest {

    Preferences prefs = Preferences.userNodeForPackage(PersistentPropertyTest.class);
    private Module testModule;
    private Injector injector;

    @BeforeClass
    protected void initialize() throws BackingStoreException {
        prefs.clear();
        testModule = new AbstractModule() {
            @Override
            protected void configure() {
                bind(ClassWithPersistentProperties.class);
            }
        };
    }

    @Test
    public void testGuiceModuleInitialization() {
        final Module persistentPropertyModule = new PersistentPropertyModule();
        injector = Guice.createInjector(persistentPropertyModule, testModule);
    }

    @Test(dependsOnMethods = "testGuiceModuleInitialization")
    public void testMembersInjection() throws Exception {

        prefs.clear();
        ClassWithPersistentProperties cwppNull = injector.getInstance(ClassWithPersistentProperties.class);
        assertNotNull(cwppNull.stringValue1);
        assertNull(cwppNull.stringValue1.get());

        prefs.put("stringValue1", "whatever");
        prefs.putBoolean("booleanValue1", false);
        ClassWithPersistentProperties cwppNotNull = injector.getInstance(ClassWithPersistentProperties.class);
        assertEquals(cwppNotNull.stringValue1.get(), "whatever");
        assertEquals(cwppNotNull.booleanValue1.get(), false);

    }

    @Test(dependsOnMethods = "testMembersInjection")
    public void testPreferencesSynchronization() throws Exception {

        prefs.clear();
        final ClassWithPersistentProperties cwpp = injector.getInstance(ClassWithPersistentProperties.class);

        cwpp.stringValue1.set("yeehhaaa!!1");
        cwpp.booleanValue1.set(true);
        assertEquals(prefs.get("stringValue1", null), "yeehhaaa!!1");
        assertEquals(prefs.getBoolean("booleanValue1", false), true);

        cwpp.stringValue1.set("yeehhaaa!!2");
        cwpp.booleanValue1.set(false);
        assertEquals(prefs.get("stringValue1", null), "yeehhaaa!!2");
        assertEquals(prefs.getBoolean("booleanValue1", true), false);

    }


    static class ClassWithPersistentProperties {
        @PersistentProperty(clazz = PersistentPropertyTest.class, key = "stringValue1")
        private final StringProperty stringValue1 = new SimpleStringProperty();
        @PersistentProperty(clazz = PersistentPropertyTest.class, key = "booleanValue1")
        private final BooleanProperty booleanValue1 = new SimpleBooleanProperty();
        ClassWithPersistentProperties() {
            super();
        }
    }
}
