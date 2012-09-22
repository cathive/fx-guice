package com.cathive.fx.guice;

import java.lang.reflect.Field;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

/**
 * 
 * @author Benjamin P. Jung
 */
class FXMLControllerTypeListener implements TypeListener {

    private final FXMLLoadingScope fxmlLoadingScope;

    FXMLControllerTypeListener(final FXMLLoadingScope fxmlLoadingScope) {
        super();
        this.fxmlLoadingScope = fxmlLoadingScope;
    }

    @Override
    public <T> void hear(TypeLiteral<T> typeLiteral, TypeEncounter<T> typeEncounter) {
        for (final Field field : typeLiteral.getRawType().getDeclaredFields()) {
            if (field.isAnnotationPresent(FXMLController.class)) {
                final FXMLController annotation = field.getAnnotation(FXMLController.class);
                typeEncounter.register(new FXMLControllerMembersInjector<T>(field, annotation, fxmlLoadingScope));
            }
        }
    }

}
