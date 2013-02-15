package com.cathive.fx.guice.example;

import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.SceneBuilder;
import javafx.stage.Stage;
import javafx.stage.StageBuilder;

import javax.inject.Inject;
import javax.inject.Named;

import com.cathive.fx.guice.GuiceApplication;
import com.cathive.fx.guice.GuiceFXMLLoader;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.name.Names;

/**
 * A simple application that will utilize the {@link GuiceFXMLLoader}
 * class to construct a nice user interface.
 * 
 * @author Benjamin P. Jung
 */
public final class FxmlExampleApp extends GuiceApplication {

    @Inject
    private GuiceFXMLLoader fxmlLoader;

    @Inject
    @Named("i18n-resources")
    private ResourceBundle resources;

    @Override
    public void init(final List<Module> modules) throws Exception {
        modules.add(new FxmlExampleModule());
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {

        // Load the FXML file that contains our user interface definition
        // Note that we use the already injected resource bundle for
        // internationalization functionality.
        final Parent root = fxmlLoader.load(getClass().getResource("FxmlExampleApp.fxml"), resources).getRoot();

        // Put the loaded user interface onto the primary stage.
        StageBuilder.create()
        .title(resources.getString("APP_TITLE"))
        .resizable(false)
        .scene(SceneBuilder.create()
            .root(root)
            .build())
        .applyTo(primaryStage);

        // Show the primary stage
        primaryStage.show();

    }


    private static class FxmlExampleModule extends AbstractModule {
        @Override
        protected void configure() {

            // Binds our resource bundle that contains localized Strings
            bind(ResourceBundle.class).annotatedWith(Names.named("i18n-resources"))
                .toInstance(ResourceBundle.getBundle(FxmlExampleApp.class.getName()));

            // Binds the button colors correctly
            bind(String.class).annotatedWith(Names.named("red-button-color-string"))
                .toInstance("#ff0000");
            bind(String.class).annotatedWith(Names.named("green-button-color-string"))
            .toInstance("#00ff00");
        }
    }


    public static void main(String[] args) {
        Application.launch(args);
    }

}
