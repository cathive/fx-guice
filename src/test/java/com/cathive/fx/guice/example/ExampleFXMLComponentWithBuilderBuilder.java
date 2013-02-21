package com.cathive.fx.guice.example;

import java.util.Set;
import java.util.Map.Entry;

import org.testng.log4testng.Logger;

import com.cathive.fx.guice.FXMLComponentBuilder;

/**
 * 
 * @author Benjamin P. Jung
 */
@SuppressWarnings("hiding")
public class ExampleFXMLComponentWithBuilderBuilder extends FXMLComponentBuilder<ExampleFXMLComponentWithBuilder> {

    /** Logger for this class */
    private final Logger LOGGER = Logger.getLogger(ExampleFXMLComponentWithBuilder.class);

    private Integer someInt;

    protected ExampleFXMLComponentWithBuilderBuilder() {
        super(ExampleFXMLComponentWithBuilder.class);
    }

    public static ExampleFXMLComponentWithBuilderBuilder create() {
        return new ExampleFXMLComponentWithBuilderBuilder();
    }

    public ExampleFXMLComponentWithBuilderBuilder someInt(int someInt) {
        LOGGER.info(String.format("Calling method 'someInt(%s)'", someInt));
        this.someInt = someInt;
        return this;
    }

    @Override
    public void applyTo(ExampleFXMLComponentWithBuilder component) {
        component.setBuilder(this);
        if (someInt != null) {
            component.setSomeInt(someInt);
        }
    }

    @Override
    public Object put(String key, Object value) {
        switch (key) {
        case "someInt":
            if (value instanceof Integer) {
                this.someInt = (Integer) value;
            } else  if (value instanceof String) {
                this.someInt = Integer.valueOf((String) value);
            }
            break;
        default:
            throw new IllegalArgumentException("Unknown Font property: " + key);
        }
        return null;
    }

    @Override
    public final Set<Entry<String, Object>> entrySet() {
        throw new UnsupportedOperationException();
    }

}
