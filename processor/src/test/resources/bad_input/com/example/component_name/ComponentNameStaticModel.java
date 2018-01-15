package com.example.component_name;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentNameRef;

@ArezComponent
public class ComponentNameStaticModel
{
  @Action
  void myAction()
  {
  }

  @ComponentNameRef
  static String getTypeName()
  {
    return null;
  }
}
