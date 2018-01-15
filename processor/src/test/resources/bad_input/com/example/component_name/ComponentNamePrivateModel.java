package com.example.component_name;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentNameRef;

@ArezComponent
public class ComponentNamePrivateModel
{
  @Action
  void myAction()
  {
  }

  @ComponentNameRef
  private String getTypeName()
  {
    return null;
  }
}
