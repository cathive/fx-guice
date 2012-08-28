package com.cathive.fx.guice.example;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.inject.Inject;
import javax.inject.Singleton;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

/**
 * 
 * @author Benjamin P. Jung
 */
@Singleton
public class ExamplePaneController implements Initializable {

	private List<String> methodCalls = new ArrayList<>();

	private boolean initialized = false;
	private boolean injected = false;

	@FXML private AnchorPane rootPane;

	@Inject private void postConstruct() {
		methodCalls.add("postConstruct()");
		this.injected = true;
	}

	@Override public void initialize(URL url, ResourceBundle rb) {
		methodCalls.add("initialize(java.net.URL, java.util.ResourceBundle)");
		this.initialized = true;
	}

	public boolean isInitialized() {
		return this.initialized;
	}

	public boolean isInjected() {
		return this.injected;
	}

	public List<String> getMethodCalls() {
		return this.methodCalls;
	}

	public AnchorPane getRootPane() {
		return this.rootPane;
	}

}
