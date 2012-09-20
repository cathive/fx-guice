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
