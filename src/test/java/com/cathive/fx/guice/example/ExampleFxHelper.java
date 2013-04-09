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

package com.cathive.fx.guice.example;

import static org.testng.Assert.*;

import java.io.IOException;

import javafx.application.Platform;

import com.cathive.fx.guice.FxApplicationThread;

/**
 * This little helper has some methods annotated with
 * {@link FxApplicationThread}.
 * 
 * @author Benjamin P. Jung
 */
public class ExampleFxHelper {

    @FxApplicationThread
    public void methodAnnotatedWithFxAppThread1() {
        assertTrue(Platform.isFxApplicationThread());
        return;
    }

    @FxApplicationThread
    public Void methodAnnotatedWithFxAppThread2() {
        assertTrue(Platform.isFxApplicationThread());
        return null;
    }

    @FxApplicationThread
    public void methodAnnotatedWithFxAppThreadThatThrowsException() throws IOException {
        throw new IOException("This exception should never be thrown, because the method declares a checked exception!");
    }
    @FxApplicationThread
    public Object methodAnnotatedFxAppThreadWithReturnType() {
        throw new IllegalStateException("This exception should never be thrown, because the method has a non-void return type!");
    }
    public void methodNotAnnotatedWithFxAppThread() {
        assertFalse(Platform.isFxApplicationThread());
        return;
    }

}
