package com.cathive.fx.guice.controllerlookup;


import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;

/**
 * {@link Module} enabling interaction between multiple controllers loaded in
 * one control.
 * 
 * @author Andy Till
 * 
 */
public class FXMLLoadingModule extends AbstractModule {

    private final FXMLLoadingScope fxmlLoadingScope = new FXMLLoadingScope();

    @Override
    protected void configure() {
        bindScope(FXMLScoped.class, fxmlLoadingScope);
        
        bind(FXMLLoadingScope.class).toInstance(fxmlLoadingScope);
    }
    
    @Provides
    public ControllerLookup provideControllerLookup() {
        if(!fxmlLoadingScope.isInScope()) {
            throw new IllegalStateException("A ControllerLookup instance cannot be injected while outside of the FXML Loading scope.");
        }
        
        return new ControllerLookup(fxmlLoadingScope.getControllers());
    }
}
