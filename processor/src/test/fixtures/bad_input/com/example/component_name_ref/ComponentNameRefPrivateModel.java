package com.example.component_name_ref;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentNameRef;

@ArezComponent
public abstract class ComponentNameRefPrivateModel
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
