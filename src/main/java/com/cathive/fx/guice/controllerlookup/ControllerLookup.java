package com.cathive.fx.guice.controllerlookup;

import java.util.List;

public class ControllerLookup {
    private final List<IdentifiableController> identifiables;

    public ControllerLookup(List<IdentifiableController> identifiables) {
        this.identifiables = identifiables;
    }
    
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