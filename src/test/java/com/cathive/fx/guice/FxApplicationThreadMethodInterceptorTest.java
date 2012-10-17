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

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.internal.MethodInstance;

import com.cathive.fx.guice.example.ExampleFxHelper;
import com.cathive.fx.guice.example.ExamplePaneController;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Scopes;
import com.google.inject.matcher.Matchers;

/**
 * @author Benjamin P. Jung
 */
@Test(singleThreaded = true)
public class FxApplicationThreadMethodInterceptorTest {

    private Injector injector;

    @BeforeClass
    protected void initialize() {
        injector = Guice.createInjector(new FXMLLoadingModule(), new AbstractModule() {
            @Override
            protected void configure() {
                bindInterceptor(
                        Matchers.any(),
                        Matchers.annotatedWith(FxApplicationThread.class), 
                        new FxApplicationThreadMethodInterceptor());
            }
        });
    }


    @Test(description = "Make sure that methods annotated with @FxApplicationThread can be identified correctly during runtime!")
    public void testAnnotationRetention() throws NoSuchMethodException, SecurityException {
        final FxApplicationThread annotation = ExampleFxHelper.class.getMethod("methodAnnotatedWithFxAppThread").getAnnotation(FxApplicationThread.class);
        assertNotNull(annotation, "No @FxApplicationThread annotation found. This probably means, that someone changed the 'Retention' of the class from 'RetentionPolicy.RUNTIME' to something useless!");
    }

    @Test
    public void testInterceptorWithNonAnnotatedMethod() {
        injector.getInstance(ExampleFxHelper.class).methodNotAnnotatedWithFxAppThread();
    }

    @Test(dependsOnMethods = "testAnnotationRetention", expectedExceptions = RuntimeException.class)
    public void testInterceptorWithAnnotatedMethodWithReturnType() {
        injector.getInstance(ExampleFxHelper.class).methodAnnotatedFxAppThreadWithReturnType();
    }

    @Test(dependsOnMethods = "testAnnotationRetention", expectedExceptions = RuntimeException.class)
    public void testInterceptorWithAnnotatedMethodThatThrowsException() throws Exception {
        injector.getInstance(ExampleFxHelper.class).methodAnnotatedWithFxAppThreadThatThrowsException();
    }


}
