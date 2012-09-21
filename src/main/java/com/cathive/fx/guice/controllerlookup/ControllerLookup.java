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

import java.util.List;

/**
 * A store of all controller instances that have been injected while an FXML
 * control was being created.
 * <p>
 * {@link ControllerLookup} must only be injected into controllers that are
 * created while an FXML control is being created.
 * 
 * @author Andy Till
 * 
 */
public class ControllerLookup {
    
    private final List<IdentifiableController> identifiables;

    public ControllerLookup(List<IdentifiableController> identifiables) {
        this.identifiables = identifiables;
    }
    
    /**
     * Returns a controller instance with the given ID.
     * 
     * @param id
     *            The string ID of the controller as returned by
     *            {@link IdentifiableController#getId()}
     * @return
     * @throws IllegalArgumentException
     *             thrown if a controller cannot be found with the given ID.
     */
    @SuppressWarnings("unchecked")
    public <T> T lookup(String id) {
        for (IdentifiableController controller : identifiables) {
            if(controller.getId().equals(id)) {
                return (T) controller;
            }
        }
        throw new IllegalArgumentException("Could not find a controller with the ID '" + id + "'");
    }
}