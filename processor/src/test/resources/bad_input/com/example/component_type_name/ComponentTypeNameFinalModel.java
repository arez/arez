package com.example.component_type_name;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentTypeName;

@ArezComponent
public class ComponentTypeNameFinalModel
{
  @Action
  void myAction()
  {
  }

  @ComponentTypeName
  final String getTypeName()
  {
    return null;
  }
}
