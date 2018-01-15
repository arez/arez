package com.example.component_type_name;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentTypeNameRef;

@ArezComponent
public class ComponentTypeNameFinalModel
{
  @Action
  void myAction()
  {
  }

  @ComponentTypeNameRef
  final String getTypeName()
  {
    return null;
  }
}
