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

package com.cathive.fx.guice.controllerlookup;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ParentIDFinderTest {

	@BeforeClass
	public void setup(){
		// this way the tests are running on the JavaFX application thread 
		// which is needed to instantiate Controls (like Label) in JavaFX 8. 
		new JFXPanel();
	}
	
    @Test
    public void IDFinderSearchesUpHierarchyUntilItFindsANodeWithAnID() {

        final VBox vBox1 = new VBox();
        final VBox vBox2 = new VBox();
        final Label label = new Label();

        final String id = "myVBox";
        vBox1.setId(id);
        vBox1.getChildren().add(vBox2);

        vBox2.getChildren().add(label);

        final String parentId = ParentIDFinder.getParentId(label);

        assertEquals(parentId, id);

    }

    @Test
    public void nullIsReturnedIfNoNodeInTheHierarchyHasAnID() {

        final VBox vBox1 = new VBox();
        final VBox vBox2 = new VBox();
        Label label = new Label();

        vBox1.getChildren().add(vBox2);

        vBox2.getChildren().add(label);

        final String parentId = ParentIDFinder.getParentId(label);

        assertEquals(parentId, null);

    }

}
