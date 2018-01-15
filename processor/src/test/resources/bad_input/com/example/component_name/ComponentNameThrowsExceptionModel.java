package com.example.component_name;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentNameRef;
import java.io.IOException;

@ArezComponent
public class ComponentNameThrowsExceptionModel
{
  @Action
  void myAction()
  {
  }

  @ComponentNameRef
  String getTypeName()
    throws IOException
  {
    return null;
  }
}
