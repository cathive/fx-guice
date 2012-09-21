package com.cathive.fx.guice;


import com.cathive.fx.guice.controllerlookup.ControllerLookup;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Provides;

/**
 * {@link Module} enabling interaction between multiple controllers loaded in
 * one control.
 * 
 * @author Andy Till
 */
public class FXMLLoadingModule extends AbstractModule {

    public FXMLLoadingModule() {
        super();
    }

    @Override
    protected void configure() {
    	final FXMLLoadingScope fxmlLoadingScope = new FXMLLoadingScope();
        bindScope(FXMLController.class, fxmlLoadingScope);
        bind(FXMLLoadingScope.class).toInstance(fxmlLoadingScope);
        bind(ControllerLookup.class).toProvider(new ControllerLookupProvider(fxmlLoadingScope));
    }


    private final class ControllerLookupProvider implements Provider<ControllerLookup> {
        private final FXMLLoadingScope fxmlLoadingScope;
        private ControllerLookupProvider(final FXMLLoadingScope fxmlLoadingScope) {
            super();
            this.fxmlLoadingScope = fxmlLoadingScope;
        }
        @Override
        public ControllerLookup get() {
            if(!fxmlLoadingScope.isInScope()) {
                throw new IllegalStateException("A ControllerLookup instance cannot be injected while outside of the FXML Loading scope.");
            }
            
            return new ControllerLookup(fxmlLoadingScope.getControllers());
        }
    }

}
