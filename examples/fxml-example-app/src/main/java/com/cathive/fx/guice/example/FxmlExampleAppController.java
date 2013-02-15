package com.cathive.fx.guice.example;

import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javax.inject.Inject;
import javax.inject.Named;

import com.cathive.fx.guice.FXMLController;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

/**
 * Example FXML controller class.
 * <p>This class will be automatically bound be Guice and can be used in
 * you FXML's "fx:controller" definition.</p>
 * 
 * @author Benjamin P. Jung
 */
@FXMLController
public final class FxmlExampleAppController {

    @Inject
    private Injector injector;

    @Inject
    @Named("i18n-resources")
    private ResourceBundle resources;

    @FXML
    private Button redButton;

    @FXML
    private Button greenButton;

    @FXML
    private Label statusLabel;

    @FXML
    void redButtonPressed(final ActionEvent e) {
        statusLabel.setText(resources.getString("ON_RED_BUTTON_PRESSED"));
    }

    @FXML
    void greenButtonPressed(final ActionEvent e) {
        statusLabel.setText(resources.getString("ON_GREEN_BUTTON_PRESSED"));
    }

    /**
     * This method will automatically be called by the FXML loader.
     * See Oracle's JavaFX/FXML documentation for further details about
     * that mechanism. (Note: it was necessary to implement {@ link Initializable})
     * int the past, but that mechanism has been deprecated.
     */
    public void initialize() {

        // Use the guice injector to fetch our color code definitions.
        final String red = injector.getInstance(Key.get(String.class, Names.named("red-button-color-string")));
        final String green = injector.getInstance(Key.get(String.class, Names.named("green-button-color-string")));
        
        // We use the Guice-injected value for our desired color to update
        // the style of our FXML-injected button.
        redButton.setStyle(String.format("-fx-base: %s;", red));
        greenButton.setStyle(String.format("-fx-base: %s;", green));
    }

}
