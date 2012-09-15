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