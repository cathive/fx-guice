package com.cathive.fx.guice;

import java.util.Locale;
import java.util.ResourceBundle;

import javafx.scene.Node;
import javafx.stage.Stage;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.cathive.fx.guice.controllerlookup.ControllerLookup;
import com.cathive.fx.guice.lookupexample.InnerLookupController;
import com.cathive.fx.guice.lookupexample.OuterLookupController;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.ProvisionException;

import static org.testng.Assert.*;

public class ControllerLookupApplicationTest {

    static class ControllerLookupGuiceApplication extends GuiceApplication {
        @Override
        public void start(Stage primaryStage) throws Exception {
            // Do nothing...
        }

        @Override
        public Injector createInjector() {
            return Guice.createInjector();
        }
    }
    
    private ControllerLookupGuiceApplication app;
    
    private GuiceFXMLLoader loader;

    private FXMLResult<Node> result;
    
    @BeforeMethod
    public void setup() throws Exception {
        app = new ControllerLookupGuiceApplication();
        app.init();

        loader = new GuiceFXMLLoader(app.getInjector());
        
        result = loader.loadWithController(
            getClass().getResource("/com/cathive/fx/guice/lookupexample/OuterLookupPane.fxml"), 
            ResourceBundle.getBundle("ExamplePane", Locale.ENGLISH)
        );
    }
    
    @Test
    public void parentControllerCanLookupNestedControllers() throws Exception {
        
        OuterLookupController controller = (OuterLookupController) result.getController();
        InnerLookupController controllerForPane1 = controller.getControllerForPane1();
        InnerLookupController controllerForPane2 = controller.getControllerForPane2();
        
        assertNotNull(controllerForPane1);
        assertNotNull(controllerForPane2);
    }
    
    @Test(expectedExceptions={IllegalArgumentException.class})
    public void illegalArgumentExceptionThrownWhenNoControllerExistsWithMatchingID() throws Exception {
        
        OuterLookupController controller = (OuterLookupController) result.getController();
        
        controller.getAnyController("Frederick");
    }
    
    @Test
    public void provisionExceptionThrownInjectingAControllerLookupOutsideOfScope() throws Exception {
        boolean threw = false;
        try {
            app.getInjector().getInstance(HasControllerLookup.class);
        } catch (ProvisionException e) {
            // a provision exception is thrown by Guice, the cause is the
            // IllegalStateException thrown by the module because a
            // ControllerLookup cannot be injected while not loading an FXML control.
            assertTrue(e.getCause() instanceof IllegalStateException);
            threw = true;
        }
        assertTrue(threw);
    }
    
    static class HasControllerLookup {
        @Inject
        ControllerLookup controllerLookup;
    }
}
