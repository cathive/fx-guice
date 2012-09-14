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

import static org.testng.Assert.*;

import javafx.stage.Stage;

import org.testng.annotations.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

/**
 * @author Benjamin P. Jung
 */
@Test(singleThreaded = true)
public class GuiceApplicationTest {

    public static final String HELLO_KEY = "hello";
    public static final String HELLO_VALUE = "Hello, World!";

    @Test(expectedExceptions = IllegalStateException.class)
    public void testInitializationWithNullArg() throws Exception {
        new GuiceApplication() {
            @Override
            public void start(Stage arg0) throws Exception {
                // Do nothing...
            }

            @Override
            public Injector createInjector() {
                // Return null to cause IllegalArgumentException
                return null;
            }
        }.init();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testInitializationWithAppWithInjectedConstructors() throws Exception {
        final InvalidGuiceApplicationWithWrongConstructor app = new InvalidGuiceApplicationWithWrongConstructor();
        app.init();
    }

    @Test(dependsOnMethods = "testInitializationWithNullArg")
    public void testInitializationWithValidInjector() throws Exception {

        final ValidGuiceApplication app = new ValidGuiceApplication();
        app.init();

        assertNotNull(app.getInjector());

        final GuiceApplication a1 = app.getInjector().getInstance(ValidGuiceApplication.class);
        final GuiceApplication a2 = app.getInjector().getInstance(app.getClass());

        assertNotNull(a1);
        assertNotNull(a2);

        assertEquals(a1, app);
        assertTrue(a1 == app);
        assertEquals(a2, app);
        assertTrue(a2 == app);

        assertNotNull(app.helloString);
        assertEquals(app.helloString, HELLO_VALUE);
    }

    static class InvalidGuiceApplicationWithWrongConstructor extends GuiceApplication {

        @Inject
        public InvalidGuiceApplicationWithWrongConstructor() {
            super();
            // Invalid constructor, because it is annotated with @Inject.
        }

        @Override
        public Injector createInjector() {
            return Guice.createInjector(new AbstractModule() {
                @Override
                protected void configure() {
                    // Intentionally left empty!
                }
            });
        }
        @Override
        public void start(Stage primaryStage) throws Exception {
            // Do nothing...
        }
    }

    static class ValidGuiceApplication extends GuiceApplication {

        @Inject
        @Named(HELLO_KEY)
        String helloString;

        @Override
        public void start(Stage primaryStage) throws Exception {
            // Do nothing...
        }

        @Override
        public Injector createInjector() {
            return Guice.createInjector(new AbstractModule() {
                @Override
                protected void configure() {
                    bind(String.class).annotatedWith(Names.named(HELLO_KEY)).toInstance(HELLO_VALUE);
                }
            });
        }

    }

}
