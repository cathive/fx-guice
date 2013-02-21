package com.cathive.fx.guice.example;

import com.cathive.fx.guice.FXMLComponent;

import javafx.scene.layout.VBox;
import javafx.util.Builder;

/**
 * 
 * @author Benjamin P. Jung
 */
@FXMLComponent(location = "/ExampleFXMLComponentWithBuilder.fxml", resources = "ExampleFXMLComponentWithBuilder", builderClass = ExampleFXMLComponentWithBuilderBuilder.class)
public class ExampleFXMLComponentWithBuilder extends VBox {

    private Builder<ExampleFXMLComponentWithBuilder> builder = null;
    public void setBuilder(Builder<ExampleFXMLComponentWithBuilder> builder) {
        this.builder = builder;
    }
    public Builder<ExampleFXMLComponentWithBuilder> getBuilder() {
        return this.builder;
    }

}
