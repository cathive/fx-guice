package com.cathive.fx.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;

import javafx.application.Application;

/**
 * @author Benjamin P. Jung
 */
public abstract class GuiceApplication extends Application {

    /**
     * The Guice Injector instance that is being used within
     * the context of this application.
     * @see #createInjector()
     * @see #getInjector()
     */
    private Injector injector;

    /**
     * To make sure that the initialization of Guice-based JavaFX application
     * works flawlessly, the original init method of the base JavaFX Application
     * class is overwritten here. All of the 
     */
    @Override
    public final void init() throws Exception {

        super.init();

        @SuppressWarnings("unchecked")
        final Class<GuiceApplication> clazz = (Class<GuiceApplication>) getClass();
        final GuiceApplication instance = this;

        final Injector inj = createInjector();
        if (inj == null) {
            throw new IllegalStateException("Injector has not been created (yet).");
        }

        // Inject all fields annotated with @Inject into this GuiceApplication instance.
        inj.injectMembers(instance);

        // Create a child injector and bind the instance of this GuiceApplication
        // to any instances of GuiceApplication.class.
        injector = inj.createChildInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(clazz).toInstance(instance);
            }
        });

    }

    /**
     * This method initializes the Guice Injector to be used for
     * dependency injection in the context of this application instance.
     * <p>This method <strong>must not</strong> return <code>null</code>.
     * @return
     *     The injector to be used in context of this application.
     */
    public abstract Injector createInjector();

    /**
     * Returns the Google Guice Injector that is used within the context
     * of this JavaFX Application.
     * @return
     *     The Guice Injector that has been created during the initialization
     *     of this JavaFX Application.
     * @see #createInjector()
     */
    public final Injector getInjector() {
        return this.injector;
    }

}
