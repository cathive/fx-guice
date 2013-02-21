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
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;

import javafx.scene.Parent;
import javafx.util.BuilderFactory;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.cathive.fx.guice.example.ExampleFXMLComponentWithBuilder;
import com.cathive.fx.guice.example.ExampleFXMLComponentWithoutBuilder;
import com.cathive.fx.guice.example.ExampleFXMLComponentWrapperController;
import com.cathive.fx.guice.fxml.FXMLLoadingModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * 
 * @author Benjamin P. Jung
 */
@Test
public class FXMLComponentTest {

    private Injector injector;

    private ExampleFXMLComponentWithoutBuilder simpleComponent;
    private ExampleFXMLComponentWithBuilder builtComponent;

    @BeforeTest
    protected void initialize() {
        this.injector = Guice.createInjector(new FXMLLoadingModule());
    }

    @Test(description = "Test the instantiation of classes annotated with @FXMLComponent via Google Guice")
    public void testSimpleComponentInstantiation() {

        simpleComponent = injector.getInstance(ExampleFXMLComponentWithoutBuilder.class);

        assertNotNull(simpleComponent);
        assertTrue(simpleComponent.isInitialized());
        assertNotNull(simpleComponent.getLabel());
        assertNotNull(simpleComponent.getButton());

    }

    @Test(dependsOnMethods = "testSimpleComponentInstantiation")
    public void testComponentWithBuilderInstantiation() {

        builtComponent = injector.getInstance(ExampleFXMLComponentWithBuilder.class);

        assertNotNull(builtComponent);
        assertNotNull(builtComponent.getBuilder());
        assertEquals(builtComponent.getSomeInt(), 42);

    }

    @Test(dependsOnMethods = "testComponentWithBuilderInstantiation")
    public void testComponentInstantiationWithinFxmlFile() throws IOException {

        final GuiceFXMLLoader fxmlLoader = injector.getInstance(GuiceFXMLLoader.class);
        
        final BuilderFactory builderFactory = injector.getInstance(BuilderFactory.class);
        assertNotNull(builderFactory.getBuilder(ExampleFXMLComponentWithBuilder.class));

        final GuiceFXMLLoader.Result loadingResult = fxmlLoader.load(getClass().getResource("/ExampleFXMLComponentWrapper.fxml"));

        final Parent root = loadingResult.getRoot();
        final ExampleFXMLComponentWrapperController ctrl = loadingResult.getController();

        assertNotNull(root);
        assertNotNull(ctrl);

        assertNotNull(ctrl.getInjector());
        assertNotNull(ctrl.getSimpleComponent());
        assertNotNull(ctrl.getBuiltComponent());
        assertNotNull(ctrl.getBuiltComponent().getBuilder());
        assertEquals(ctrl.getBuiltComponent().getSomeInt(), 43);

    }

}
