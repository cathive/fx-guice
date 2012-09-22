package com.cathive.fx.guice;

import java.lang.reflect.Field;

import com.cathive.fx.guice.controllerlookup.ControllerLookup;
import com.google.inject.MembersInjector;

/**
 * 
 * @author Benjamin P. Jung
 */
class FXMLControllerMembersInjector<T> implements MembersInjector<T> {

    private final Field field;
    private final FXMLController annotation;
    private final FXMLLoadingScope scope;

    FXMLControllerMembersInjector(final Field field, final FXMLController annotation, final FXMLLoadingScope scope) {
        super();
        this.field = field;
        this.annotation = annotation;
        this.scope = scope;
        field.setAccessible(true);
    }

    @Override
    public void injectMembers(T instance) {
        Object controllerInstance = null;
        if (!annotation.controllerId().isEmpty()) {
            controllerInstance = new ControllerLookup(scope.getIdentifiables()).lookup(annotation.controllerId());
        } else {
            controllerInstance = scope.getInstance(annotation.controllerId());
        }
        if (controllerInstance == null) {
            throw new IllegalStateException("No suitable controller instance found!");
        }
        try {
            field.set(instance, controllerInstance);
          } catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
          }
    }

}
