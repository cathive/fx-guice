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

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.stage.Stage;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.cathive.fx.guice.controllerlookup.ControllerLookup;
import com.cathive.fx.guice.lookupexample.InnerLookupController;
import com.cathive.fx.guice.lookupexample.OuterLookupController;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.ProvisionException;

public class ControllerLookupApplicationTest {

    static class ControllerLookupGuiceApplication extends GuiceApplication {
        @Override
        public void start(Stage primaryStage) throws Exception {
            // Do nothing...
        }

        @Override
        public void init(List<Module> modules) throws Exception {
            // Intentionally left empty
        }
    }
    
    private ControllerLookupGuiceApplication app;
    
    private GuiceFXMLLoader loader;

    private GuiceFXMLLoader.Result result;
    
    @BeforeMethod
    public void setup() throws Exception {
        app = new ControllerLookupGuiceApplication();
        app.init();

        loader = app.getInjector().getInstance(GuiceFXMLLoader.class);
        
        result = loader.load(
            getClass().getResource("/OuterLookupPane.fxml"), 
            ResourceBundle.getBundle("ExamplePane", Locale.ENGLISH)
        );
    }
    
    @Test
    public void parentControllerCanLookupNestedControllers() throws Exception {
        
        OuterLookupController controller = result.getController();
        InnerLookupController controllerForPane1 = controller.getControllerForPane1();
        InnerLookupController controllerForPane2 = controller.getControllerForPane2();
        
        assertNotNull(controllerForPane1);
        assertNotNull(controllerForPane2);
    }
    
    @Test(expectedExceptions={IllegalArgumentException.class})
    public void illegalArgumentExceptionThrownWhenNoControllerExistsWithMatchingID() throws Exception {
        
        OuterLookupController controller = result.getController();
        
        controller.getAnyController("Frederick");
    }
    
    @Test
    public void provisionExceptionThrownInjectingAControllerLookupOutsideOfScope() throws Exception {
        boolean threw = false;
        try {
            app.getInjector().getInstance(HasControllerLookup.class);
        } catch (ProvisionException e) {
            // a provision exception is thrown by Guice, the cause is the
            // IllegalStateException thrown by the module because a
            // ControllerLookup cannot be injected while not loading an FXML control.
            assertTrue(e.getCause() instanceof IllegalStateException);
            threw = true;
        }
        assertTrue(threw);
    }
    
    static class HasControllerLookup {
        @Inject
        ControllerLookup controllerLookup;
    }
}
