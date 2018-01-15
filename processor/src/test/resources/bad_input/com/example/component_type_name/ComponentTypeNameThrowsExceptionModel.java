package com.example.component_type_name;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentTypeNameRef;
import java.io.IOException;

@ArezComponent
public class ComponentTypeNameThrowsExceptionModel
{
  @Action
  void myAction()
  {
  }

  @ComponentTypeNameRef
  String getTypeName()
    throws IOException
  {
    return null;
  }
}
