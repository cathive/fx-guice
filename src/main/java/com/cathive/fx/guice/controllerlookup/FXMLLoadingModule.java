package com.cathive.fx.guice.controllerlookup;


import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class FXMLLoadingModule extends AbstractModule {

    private final FXMLLoadingScope fxmlLoadingScope = new FXMLLoadingScope();

    @Override
    protected void configure() {
        bindScope(FXMLScoped.class, fxmlLoadingScope);
        
        bind(FXMLLoadingScope.class).toInstance(fxmlLoadingScope);
    }
    
    @Provides
    public ControllerLookup provideControllerLookup() {
        return new ControllerLookup(fxmlLoadingScope.getControllers());
    }
}
