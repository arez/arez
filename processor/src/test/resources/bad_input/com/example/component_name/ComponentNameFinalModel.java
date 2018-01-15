package com.example.component_name;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentNameRef;

@ArezComponent
public class ComponentNameFinalModel
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
