package com.cathive.fx.guice.example;

import com.cathive.fx.guice.FXMLComponentBuilder;

/**
 * 
 * @author Benjamin P. Jung
 */
public class ExampleFXMLComponentWithBuilderBuilder extends FXMLComponentBuilder<ExampleFXMLComponentWithBuilder> {

    protected ExampleFXMLComponentWithBuilderBuilder() {
        super(ExampleFXMLComponentWithBuilder.class);
    }

    public static ExampleFXMLComponentWithBuilderBuilder create() {
        return new ExampleFXMLComponentWithBuilderBuilder();
    }

    @Override
    public void applyTo(ExampleFXMLComponentWithBuilder component) {
        component.setBuilder(this);
    }

}
