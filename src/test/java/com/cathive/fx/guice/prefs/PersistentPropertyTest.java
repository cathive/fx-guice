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

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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
        assertNotNull(cwppNull.value1);
        assertNull(cwppNull.value1.get());

        prefs.put("value1", "whatever");
        ClassWithPersistentProperties cwppNotNull = injector.getInstance(ClassWithPersistentProperties.class);
        assertNotNull(cwppNotNull.value1);
        assertNotNull(cwppNotNull.value1.get());
        assertEquals(cwppNotNull.value1.get(), "whatever");

    }


    static class ClassWithPersistentProperties {
        @PersistentProperty(clazz = PersistentPropertyTest.class, key = "value1")
        private final StringProperty value1 = new SimpleStringProperty();
        ClassWithPersistentProperties() {
            super();
        }
    }
}
