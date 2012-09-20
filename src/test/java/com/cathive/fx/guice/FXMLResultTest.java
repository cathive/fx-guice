package com.cathive.fx.guice;

import javafx.scene.Node;

import org.testng.annotations.Test;

public class FXMLResultTest {

  @Test
  public void testToString() {
      new FXMLResult<Node>(null, null).toString();
  }
}
