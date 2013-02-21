package com.cathive.fx.guice.example;

import org.testng.log4testng.Logger;

import com.cathive.fx.guice.FXMLComponent;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.layout.VBox;
import javafx.util.Builder;

/**
 * 
 * @author Benjamin P. Jung
 */
@FXMLComponent(location = "/ExampleFXMLComponentWithBuilder.fxml",
               resources = "ExampleFXMLComponentWithBuilder",
               builderClass = ExampleFXMLComponentWithBuilderBuilder.class
)
public class ExampleFXMLComponentWithBuilder extends VBox {

    /** Logger for this class */
    private static final Logger LOGGER = Logger.getLogger(ExampleFXMLComponentWithBuilder.class);

    public final IntegerProperty someInt = new SimpleIntegerProperty();

    private Builder<ExampleFXMLComponentWithBuilder> builder = null;
    public void setBuilder(Builder<ExampleFXMLComponentWithBuilder> builder) {
        this.builder = builder;
    }
    public Builder<ExampleFXMLComponentWithBuilder> getBuilder() {
        return this.builder;
    }

    public int getSomeInt() {
        LOGGER.info("Calling 'getSomeInt()'");
        return this.someInt.get();
    }

    public void setSomeInt(final int someInt) {
        LOGGER.info(String.format("Calling 'setSomeInt(%s)'", someInt));
        this.someInt.set(someInt);
    }

    public IntegerProperty someIntProperty() {
        LOGGER.info("Calling 'someIntProperty()'");
        return this.someInt;
    }

}
