/*
 * Copyright (C) 2012 The Cat Hive Developers.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cathive.fx.guice.example;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import javax.inject.Inject;

import com.cathive.fx.guice.FXMLController;

/**
 * 
 * @author Benjamin P. Jung
 */
@FXMLController
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
