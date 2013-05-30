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

package com.cathive.fx.guice;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation marks methods that must be executed on the JavaFX application
 * thread.
 * 
 * @author Benjamin P. Jung
 * @see javafx.application.Platform#isFxApplicationThread()
 * @see javafx.application.Platform#runLater(java.lang.Runnable)
 */
@Documented
@Retention(RUNTIME)
@Target({ METHOD })
public @interface FxApplicationThread {
    // Intentionally left empty.
}
