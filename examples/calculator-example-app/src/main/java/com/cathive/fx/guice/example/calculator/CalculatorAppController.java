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
import java.text.NumberFormat;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;

import com.cathive.fx.guice.FXMLController;
import com.google.inject.Inject;

/**
 * Controller for the calculator app user interface.
 * @author Benjamin P. Jung
 */
@FXMLController
public final class CalculatorAppController {

    @Inject private CalculatorApp app;

    @FXML private BorderPane rootPane;
    @FXML private Rectangle rootPaneShape;
    @FXML private Label output;

    private int decimalPos = 0;

    @FXML
    public void onDigit(final ActionEvent event) {
        final Button button = (Button) event.getSource();
        final Integer digit = (Integer) button.getUserData();
        final BigDecimal xRegValue = app.getXRegister();
        if (app.getYRegister() == null && app.getFlagRegister() != null) {
            app.setYRegister(xRegValue);
            app.setXRegister(BigDecimal.valueOf(digit));
            this.decimalPos = 0;
        } else if (decimalPos > 0) {
            app.setXRegister(xRegValue.add(BigDecimal.valueOf(digit).divide(BigDecimal.valueOf(decimalPos))));
            decimalPos *= 10;
        } else {
            app.setXRegister(xRegValue.multiply(BigDecimal.valueOf(10)).add(BigDecimal.valueOf(digit)));
        }
        updateOutput();
    }

    @FXML
    void onDecimalMark(final ActionEvent event) {
        decimalPos = 10;
        updateOutput();
    }

    @FXML
    void onOperation(final ActionEvent event) {
        if (app.getYRegister() != null && app.getFlagRegister() != null) {
            onEnter(event);
        }
        final Button button = (Button) event.getSource();
        final Operation operation = (Operation) button.getUserData();
        app.setFlagRegister(operation);
        updateOutput();
    }

    @FXML
    void onGlobalClear(final ActionEvent event) {
        app.globalClear();
        decimalPos = 0;
        updateOutput();
    }

    @FXML
    void onEnter(final ActionEvent event) {

        if (app.getFlagRegister() == null) {
            app.setYRegister(null);
            return;
        }

        BigDecimal result;
        switch (app.getFlagRegister()) {
        case ADD:
            result = app.getYRegister().add(app.getXRegister());
            break;
        case SUBTRACT:
            result = app.getYRegister().subtract(app.getXRegister()); 
            break;
        case MULTIPLY:
            result = app.getYRegister().multiply(app.getXRegister());
            break;
        case DIVIDE:
            result = app.getYRegister().divide(app.getXRegister());
            break;
        default:
            throw new IllegalStateException();
        }

        app.setXRegister(result);
        app.setYRegister(null);
        app.setFlagRegister(null);
        decimalPos = 0;
        updateOutput();

    }

    @FXML
    void exitApplication(final ActionEvent event) {
        Platform.exit();
    }

    protected void updateOutput() {
        output.setText(NumberFormat.getInstance().format(app.getXRegister().doubleValue()));
    }

}
