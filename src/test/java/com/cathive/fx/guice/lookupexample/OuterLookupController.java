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
