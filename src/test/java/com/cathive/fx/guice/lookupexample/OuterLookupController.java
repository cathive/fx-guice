package com.cathive.fx.guice.lookupexample;

import com.cathive.fx.guice.FXMLController;
import com.cathive.fx.guice.controllerlookup.ControllerLookup;
import com.cathive.fx.guice.controllerlookup.IdentifiableController;
import com.google.inject.Inject;

@FXMLController
public class OuterLookupController {

    @Inject
    private ControllerLookup controllerLookup;
    
    public InnerLookupController getControllerForPane1() {
        return controllerLookup.lookup("examplePane1");
    }
    
    public InnerLookupController getControllerForPane2() {
        return controllerLookup.lookup("examplePane2");
    }
    
    public IdentifiableController getAnyController(String id) {
        return controllerLookup.lookup(id);
    }
}
