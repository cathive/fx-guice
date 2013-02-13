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

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation can be used to mark JavaFX Properties as persistent, thus
 * the value of these properties will be stored on your local hard drive and
 * restored the next time your application is started.
 * <p>The values for the JavaFX properties will be fetched and stored using
 * an instance of java.util.prefs.Preferences in the background.</p>
 * 
 * @see java.util.prefs.Preferences
 * 
 * @author Benjamin P. Jung
 */
@Retention(RUNTIME)
@Target({ FIELD })
public @interface PersistentProperty {

    /**
     * @see java.util.prefs.Preferences#userNodeForPackage(Class)
     * @see java.util.prefs.Preferences#systemNodeForPackage(Class)
     * @return
     *     The type of the preferences node to be used.
     */
    public NodeType type() default NodeType.USER_NODE;

    /**
     * Specifies the preference node from the calling user's preference tree that is associated
     * (by convention) with the specified class's package. The convention is as follows: the absolute
     * path name of the node is the fully qualified package name, preceded by a slash ('/'), and with
     * each period ('.') replaced by a slash. For example the absolute path name of the node associated
     * with the class com.acme.widget.Foo is /com/acme/widget.
     * 
     * @return
     *     The class to be used when looking up the package under
     *     which to store the persistent property's value.
     */
    public Class<?> clazz();

    /**
     * @return
     *     The key that will be used to store the persistent property.
     */
    public String key();

    /**
     * Enumeration over the different storage facilities for your
     * persistent properties.
     * 
     * @author Benjamin P. Jung
     */
    public enum NodeType {

        /**
         * Indicated, that the property shall be persisted using the <strong>user</strong> preference tree
         * @see java.util.prefs.Preferences#userNodeForPackage(Class)
         */
        USER_NODE,

        /**
         * Indicated, that the property shall be persisted using the </strong>system</strong> preference tree
         * @see java.util.prefs.Preferences#systemNodeForPackage(Class)
         */
        SYSTEM_NODE;

    }

}
