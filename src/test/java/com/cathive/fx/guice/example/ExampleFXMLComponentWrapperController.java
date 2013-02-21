package com.cathive.fx.guice.example;

import javafx.fxml.FXML;

import javax.inject.Inject;

import com.cathive.fx.guice.FXMLController;
import com.google.inject.Injector;

/**
 * 
 * @author Benjamin P. Jung
 */
@FXMLController
public class ExampleFXMLComponentWrapperController {

    @Inject private Injector injector;

    @FXML ExampleFXMLComponentWithoutBuilder simpleComponent;
    @FXML ExampleFXMLComponentWithBuilder builtComponent;

    public Injector getInjector() {
        return this.injector;
    }

    public ExampleFXMLComponentWithoutBuilder getSimpleComponent() {
        return this.simpleComponent;
    }

    public ExampleFXMLComponentWithBuilder getBuiltComponent() {
        return this.builtComponent;
    }

}
