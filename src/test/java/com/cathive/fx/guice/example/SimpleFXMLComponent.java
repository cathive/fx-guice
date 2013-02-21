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

package com.cathive.fx.guice.example;

import java.net.URL;
import java.util.ResourceBundle;

import javax.inject.Inject;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import com.cathive.fx.guice.FXMLComponent;
import com.google.inject.Injector;

/**
 * Example component to demonstrate and test the usage of the
 * {@link FXMLComponent} annotation.
 * 
 * @author Benjamin P. Jung
 */
@FXMLComponent(location = "/SimpleFXMLComponent.fxml", resources = "SimpleFXMLComponent")
public final class SimpleFXMLComponent extends VBox {

    private final IntegerProperty theAnswerToEverything = new SimpleIntegerProperty();

    @Inject private Injector injector;

    @FXML private URL location;
    @FXML private ResourceBundle resources;

    @FXML private Button button;
    @FXML private Label label;

    private boolean initialized = false;

    public SimpleFXMLComponent() {
        super();
    }

    public Injector getInjector() {
        return this.injector;
    }

    public void initialize() {
        this.initialized = true;
    }

    public Button getButton() {
        return this.button;
    }

    public Label getLabel() {
        return this.label;
    }

    public boolean isInitialized() {
        return this.initialized;
    }

    public int getTheAnswerToEverything() {
        return this.theAnswerToEverything.get();
    }

    public void setTheAnswerToEverything(final int theAnswerToEverything) {
        this.theAnswerToEverything.set(theAnswerToEverything);
    }

    public IntegerProperty theAnswerToEverythingProperty() {
        return this.theAnswerToEverything;
    }

}
