package com.cathive.fx.guice.controllerlookup;

import javafx.scene.Parent;

public class ParentIDFinder {
    
    /**
     * Find a non {@code null} ID on the given {@link Parent}. If the ID is
     * {@code null} then search up the graph to find a node with an ID.
     * 
     * @param node
     *            The starting node.
     * @return The ID of this node or the ID of the parent if this is
     *         {@code null}, if this is also null then the parent's parent will
     *         be returned if non-{@code null} and so on. If no parent's have an
     *         ID set then {@code null} is returned.
     */
    public static String getParentId(Parent node) {
        if(node == null) {
            return null;
        }
        else if(node.getId() != null) {
            return node.getId();
        }
        else {
            return getParentId(node.getParent());
        }
    }
}
