package com.cathive.fx.guice.controllerlookup;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ParentIDFinderTest {
    
  @Test
  public void IDFinderSearchesUpHierarchyUntilItFindsANodeWithAnID() {
      
      VBox vBox1 = new VBox();
      VBox vBox2 = new VBox();
      Label label = new Label();
      
      String id = "myVBox";
      vBox1.setId(id);
      vBox1.getChildren().add(vBox2);
      
      vBox2.getChildren().add(label);
      
      String parentId = ParentIDFinder.getParentId(label);
      
      assertEquals(parentId, id);
  }
  
  @Test
  public void nullIsReturnedIfNoNodeInTheHierarchyHasAnID() {
      
      VBox vBox1 = new VBox();
      VBox vBox2 = new VBox();
      Label label = new Label();
      
      vBox1.getChildren().add(vBox2);
      
      vBox2.getChildren().add(label);
      
      String parentId = ParentIDFinder.getParentId(label);
      
      assertEquals(parentId, null);
  }
}
