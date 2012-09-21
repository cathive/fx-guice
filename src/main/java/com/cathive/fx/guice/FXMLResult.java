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

package com.cathive.fx.guice;

import javafx.scene.Node;

/**
 * The result of an FXML file being loaded.
 * 
 * @author Andy Till
 * 
 * @param <NodeType>
 *            The control type to be returned, must be a subclass of
 *            {@link Node}.
 */
public class FXMLResult<NodeType extends Node> {

    private final NodeType node;

    private final Object controller;

    public FXMLResult(NodeType aNode, Object aController) {
        node = aNode;
        controller = aController;
    }

    public NodeType getNode() {
        return node;
    }

    public Object getController() {
        return controller;
    }

    @Override
    public String toString() {
        return "FXMLResult [node=" + node + ", controller=" + controller + "]";
    }
}
