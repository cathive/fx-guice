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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Collection;
import java.util.HashSet;
import java.util.ResourceBundle;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.cathive.fx.guice.example.ExamplePaneController;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * 
 * @author Benjamin P. Jung
 */
@Test(singleThreaded = true)
public class GuiceFXMLLoaderTest {

    // Initialized prior to all test methods!
    private Injector injector = null;

    private GuiceFXMLLoader fxmlLoader = null;

    private ExamplePaneController ctrl;

    @BeforeClass
    private void initialize() throws Exception {
    	final GuiceApplication app = new GuiceApplication () {
            @Override
            public Collection<Module> initModules() {
                return new HashSet<Module>();
            }
            @Override
            public void start(Stage arg0) throws Exception {
                // Intentionally left empty!
            }
        };
        app.init();
        this.injector = app.getInjector();
    }

    @Test(description = "Assert that an instance of the GuiceFXMLLoader without an Injector Instance cannot be created", expectedExceptions = IllegalArgumentException.class)
    public void instantiationViaConstructorTest() throws Exception {
        // Constructor call with null arguments must fail!
        new GuiceFXMLLoader(null, null);
    }

    @Test(dependsOnMethods = "instantiationViaConstructorTest")
    public void instantiationViaGuiceTest() throws Exception {
        // Creates an instance of the GuiceFXMLLoader using a Guice Injector
        fxmlLoader = injector.getInstance(GuiceFXMLLoader.class);
        assertNotNull(fxmlLoader);
    }

    @Test(dependsOnMethods = "instantiationViaGuiceTest", expectedExceptions = NullPointerException.class)
    public void fxmlLoadingTestWithNullParams() throws Exception {
        fxmlLoader.load(null);
    }

    @Test(dependsOnMethods = "instantiationViaGuiceTest")
    public void fxmlControllerInstantiationTest() throws Exception {
        // Fetches an instance of the ExamplePaneController and makes sure it is not (yet) initialized.
        ctrl = injector.getInstance(ExamplePaneController.class);
        assertNotNull(ctrl);
        assertTrue(ctrl.isInjected());
        assertFalse(ctrl.isInitialized());
        assertNull(ctrl.getRootPane());
        assertTrue(ctrl.getMethodCalls().contains("postConstruct()"));
        assertEquals(ctrl.getMethodCalls().size(), 1);
    }

    @Test(dependsOnMethods = "fxmlControllerInstantiationTest")
    public void fxmlLoadingTest() throws Exception {

        // Load the FXML file and check that afterwards the controller must be initialized.
        final GuiceFXMLLoader.Result result = fxmlLoader.load(getClass().getResource("/ExamplePane.fxml"),
                ResourceBundle.getBundle("ExamplePane"));
        assertNotNull(result);
        final AnchorPane pane = result.getRoot(); 
        ctrl = result.getController();

        assertTrue(ctrl.isInitialized());
        assertNotNull(ctrl.getRootPane());
        assertEquals(ctrl.getRootPane(), pane);
        assertTrue(ctrl.getMethodCalls().contains("initialize(java.net.URL, java.util.ResourceBundle)"));
        assertEquals(ctrl.getMethodCalls().size(), 2);

    }

}
