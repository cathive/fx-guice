package com.cathive.fx.guice.controllerlookup;

import java.util.ArrayList;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.Singleton;

@Singleton
public class FXMLLoadingScope implements Scope {

    private ArrayList<IdentifiableController> identifiables;

    public void enter() {

        identifiables = new ArrayList<IdentifiableController>();
    }

    public void exit() {

        identifiables = null;
    }

    @Override
    public <T> Provider<T> scope(final Key<T> key, final Provider<T> unscoped) {
        return new Provider<T>() {
            @Override
            @SuppressWarnings("unchecked")
            public T get() {
                return (T) provideObject(key, unscoped);
            }
        };
    }

    protected <T> Object provideObject(Key<T> key, Provider<T> unscoped) {

        Object providedObject = unscoped.get();

        if (providedObject instanceof IdentifiableController) {
            if (identifiables != null) {
                identifiables.add((IdentifiableController) providedObject);
            }
        }

        return providedObject;
    }

    public ArrayList<IdentifiableController> getControllers() {
        return identifiables;
    }
}
