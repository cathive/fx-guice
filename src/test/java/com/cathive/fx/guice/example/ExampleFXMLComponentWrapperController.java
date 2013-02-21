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

    @FXML SimpleFXMLComponent simpleComponent1;
    @FXML SimpleFXMLComponent simpleComponent2;

    public Injector getInjector() {
        return this.injector;
    }

    public SimpleFXMLComponent getSimpleComponent1() {
        return this.simpleComponent1;
    }

    public SimpleFXMLComponent getSimpleComponent2() {
        return this.simpleComponent2;
    }

}
