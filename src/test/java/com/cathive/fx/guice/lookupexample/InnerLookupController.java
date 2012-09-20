package com.cathive.fx.guice.lookupexample;

import javafx.fxml.FXML;
import javafx.scene.Parent;

import com.cathive.fx.guice.controllerlookup.FXMLScoped;
import com.cathive.fx.guice.controllerlookup.IdentifiableController;
import com.cathive.fx.guice.controllerlookup.ParentIDFinder;

@FXMLScoped
public class InnerLookupController implements IdentifiableController {

    @FXML
    private Parent root;
    
    @Override
    public String getId() {
        return ParentIDFinder.getParentId(root);
    }
}
