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

package com.cathive.fx.guice.example.calculator;

import java.math.BigDecimal;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SceneBuilder;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageBuilder;
import javafx.stage.StageStyle;

import com.cathive.fx.guice.GuiceApplication;
import com.cathive.fx.guice.GuiceFXMLLoader;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.name.Named;

/**
 * @author Benjamin P. Jung
 */
public final class CalculatorApp extends GuiceApplication {

    @Inject private GuiceFXMLLoader fxmlLoader;

    /** Resource Bundle being used to translate the application */
    @Inject @Named("resources")
    private ResourceBundle resources;

    private final ObjectProperty<BigDecimal> xRegister = new SimpleObjectProperty<>(BigDecimal.ZERO);
    private final ObjectProperty<BigDecimal> yRegister = new SimpleObjectProperty<>(null);

    /**
     * The function for the calculation is stored here until the calculator needs it.
     */
    private final ObjectProperty<Operation> flagRegister = new SimpleObjectProperty<>(null);

    @Override
    public void init(List<Module> modules) throws Exception {
        modules.add(new CalculatorAppModule());
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {

        final Parent root = fxmlLoader.load(getClass().getResource("CalculatorApp.fxml"), resources).getRoot();
        final MouseEventHandler meh = new MouseEventHandler(root);
        root.setOnMousePressed(meh);
        root.setOnMouseDragged(meh);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        StageBuilder.create()
            .title(resources.getString("APP_NAME"))
            .resizable(false)
            .scene(SceneBuilder.create()
                .fill(Color.TRANSPARENT)
                .root(root)
                .build())
            .applyTo(primaryStage);

        primaryStage.show();

    }

    public void globalClear() {
        xRegister.set(BigDecimal.ZERO);
        yRegister.set(null);
        flagRegister.set(null);
    }

    public void setXRegister(final BigDecimal xRegister) {
        this.xRegister.set(xRegister);
    }

    public BigDecimal getXRegister() {
        return this.xRegister.get();
    }

    public ObjectProperty<BigDecimal> xRegisterProperty() {
        return this.xRegister;
    }

    public void setYRegister(BigDecimal yRegister) {
        this.yRegisterProperty().set(yRegister);
    }

    public BigDecimal getYRegister() {
        return this.yRegister.get();
    }

    public ObjectProperty<BigDecimal> yRegisterProperty() {
        return this.yRegister;
    }

    public void setFlagRegister(final Operation operation) {
        this.flagRegister.set(operation);
    }

    public Operation getFlagRegister() {
        return this.flagRegister.get();
    }

    public ObjectProperty<Operation> flagRegisterProperty() {
        return this.flagRegister;
    }

    /**
     * @param args
     *        Command line arguments. Currently unused.
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    static class MouseEventHandler implements EventHandler<MouseEvent> {
        final Node node;
        double initialX;
        double initialY;
        MouseEventHandler(final Node node) {
            super();
            this.node = node;
        }
        @Override
        public void handle(MouseEvent me) {
            final EventType<?> eventType = me.getEventType();
            if (eventType.equals(MouseEvent.MOUSE_DRAGGED)) {
                node.getScene().getWindow().setX(me.getScreenX() - initialX);
                node.getScene().getWindow().setY(me.getScreenY() - initialY);   
            } else  if (eventType.equals(MouseEvent.MOUSE_PRESSED)) {
                initialX = me.getSceneX();
                initialY = me.getSceneY();
            }
        }
    }

}
