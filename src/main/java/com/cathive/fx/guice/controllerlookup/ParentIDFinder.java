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

import javafx.scene.Node;
import javafx.scene.Parent;

/**
 * 
 * @author Andy Till
 */
public final class ParentIDFinder {

    /** Private constructor - should never be needed. */
    private ParentIDFinder() {
        // Intentionally left empty.
        // No instance of this class should ever be needed.
    }

    /**
     * Find a non {@code null} ID on the given {@link Parent}. If the ID is
     * {@code null} then search up the graph to find a node with the given ID.
     * 
     * @param node
     *            The starting node.
     * @return The ID of this node or the ID of the parent if this is
     *         {@code null}, if this is also null then the parent's parent will
     *         be returned if non-{@code null} and so on. If no parent's have an
     *         ID set then {@code null} is returned.
     */
    public static String getParentId(final Node node) {
        final String parentId;
        if (node == null) {
            parentId = null;
        } else if (node.getId() != null) {
            parentId = node.getId();
        } else {
            parentId = getParentId(node.getParent());
        }
        return parentId;
    }

}
