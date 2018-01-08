package com.example.component_name;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentName;
import java.io.IOException;

@ArezComponent
public class ComponentNameThrowsExceptionModel
{
  @Action
  void myAction()
  {
  }

  @ComponentName
  String getTypeName()
    throws IOException
  {
    return null;
  }
}
