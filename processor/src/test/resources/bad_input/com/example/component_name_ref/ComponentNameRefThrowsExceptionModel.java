package com.example.component_name_ref;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentNameRef;
import java.io.IOException;

@ArezComponent
public abstract class ComponentNameRefThrowsExceptionModel
{
  @Action
  void myAction()
  {
  }

  @ComponentNameRef
  abstract String getTypeName()
    throws IOException;
}
