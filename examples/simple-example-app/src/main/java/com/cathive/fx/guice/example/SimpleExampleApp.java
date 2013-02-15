package com.cathive.fx.guice.example;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import javafx.application.Application;
import javafx.scene.SceneBuilder;
import javafx.scene.control.LabelBuilder;
import javafx.stage.Stage;
import javafx.stage.StageBuilder;

import com.cathive.fx.guice.GuiceApplication;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.name.Names;

/**
 * Simple example app to show off some of the nice features of the "CathiveFX Guice"
 * (fx-guice) library.
 * 
 * @author Benjamin P. Jung
 */
public class SimpleExampleApp extends GuiceApplication {

    @Inject
    @Named("app-title-string")
    private String appTitle;

    @Inject
    @Named("hello-world-string")
    private String helloWorldString;

    @Override
    public void init(final List<Module> modules) throws Exception {
        modules.add(new SimpleExampleModule());
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {

        // Construct a very simple example user interface.
        // Note that we can use injected fields that have been defined
        // in our example guice module after the initialization of the
        // app has been done.
        StageBuilder.create()
        .title(appTitle)
        .resizable(false)
        .scene(SceneBuilder.create()
            .root(LabelBuilder.create()
                .text(helloWorldString)
                .build())
            .build())
        .applyTo(primaryStage);

        // Show the primary stage
        primaryStage.show();
    }

    private static class SimpleExampleModule extends AbstractModule {
        @Override
        protected void configure() {
            // Binds two named String instances.
            bind(String.class).annotatedWith(Names.named("app-title-string"))
            .toInstance("CathiveFX Guice Example");
            bind(String.class).annotatedWith(Names.named("hello-world-string"))
            .toInstance("Hello World!");
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}