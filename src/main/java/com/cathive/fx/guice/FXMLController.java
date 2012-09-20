package com.cathive.fx.guice;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import com.cathive.fx.guice.controllerlookup.ControllerLookup;
import com.cathive.fx.guice.controllerlookup.IdentifiableController;
import com.google.inject.Scope;
import com.google.inject.ScopeAnnotation;

/**
 * This {@link Scope} annotation should be used on controllers which need to be
 * looked up via {@link ControllerLookup}.
 * 
 * @author Andy Till
 * 
 * @see IdentifiableController
 */
@ScopeAnnotation
@Retention(RUNTIME) 
@Target({ TYPE, METHOD }) 
public @interface FXMLController {

}
