package com.example.component_type_name;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentTypeNameRef;

@ArezComponent
public abstract class ComponentTypeNameStaticModel
{
  @Action
  void myAction()
  {
  }

  @ComponentTypeNameRef
  static String getTypeName()
  {
    return null;
  }
}
