package com.example.component_name_ref;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentNameRef;

@ArezComponent
public abstract class ComponentNameRefFinalModel
{
  @Action
  void myAction()
  {
  }

  @ComponentNameRef
  final String getTypeName()
  {
    return null;
  }
}
