package com.example.component_name_ref;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentNameRef;

@ArezComponent
public abstract class ComponentNameRefStaticModel
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
