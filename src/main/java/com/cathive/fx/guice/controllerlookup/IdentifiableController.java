package com.cathive.fx.guice.controllerlookup;

import javafx.scene.Parent;

/**
 * {@link IdentifiableController} should be implemented by JavaFX controller
 * classes that need to be looked up via {@link ControllerLookup}.
 * 
 * @author Andy Till
 * 
 * @see FXMLScoped
 * @see ParentIDFinder
 */
public interface IdentifiableController {

    /**
     * @return The string ID that can be used to lookup this controller. This
     *         could be hard coded or could return the ID set on the FXML for
     *         this control in the {@link Parent}.
     */
    String getId();
}
