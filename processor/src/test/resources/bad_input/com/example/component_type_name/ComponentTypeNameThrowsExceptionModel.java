package com.example.component_type_name;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentTypeName;
import java.io.IOException;

@ArezComponent
public class ComponentTypeNameThrowsExceptionModel
{
  @Action
  void myAction()
  {
  }

  @ComponentTypeName
  String getTypeName()
    throws IOException
  {
    return null;
  }
}
