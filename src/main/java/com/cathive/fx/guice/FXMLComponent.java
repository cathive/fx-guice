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

package com.cathive.fx.guice;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.google.inject.ScopeAnnotation;

import javafx.fxml.FXMLLoader;

/**
 * This class can be used to annotate FXML-based components.
 * @author Benjamin P. Jung
 * @since 2.0.0
 */
@Documented
@ScopeAnnotation
@Retention(RUNTIME)
@Target({ TYPE })
public @interface FXMLComponent {

    /**
     * A string definition of the URL to be used for loading the FXML file.
     * <p>The location can either be a location absolute or relative within
     * the classpath (e.g. "/com/example/MyComponent.fxml" or "MyComponent.fxml")
     * or a complete URL (e.g. "http://www.example.com/MyComponent.fxml")</p>
     * @see FXMLLoader#setLocation(java.net.URL)
     * @return
     *     A String that represents the location of the FXML file
     *     that shall be loaded.
     */
    public String location() default "";

    /**
     * Location of the {@link java.util.ResourceBundle} to be used when loading the FXML
     * document that represents this component.
     * <p>Default: none</p>
     * @see FXMLLoader#setResources(java.util.ResourceBundle)
     * @return
     *      Location of the resource bundle
     */
    public String resources() default "";

    /**
     * Character set that has to be used, when parsing the FXML input given.
     * <p>Default: UTF-8</p>
     * @see FXMLLoader#setCharset(java.nio.charset.Charset)
     * @return
     *     The charset of the FXML file
     */
    public String charset() default "UTF-8";

}
