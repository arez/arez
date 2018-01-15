package com.example.component_type_name;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentTypeNameRef;

@ArezComponent
public class ComponentTypeNameReturnNonStringModel
{
  @Action
  void myAction()
  {
  }

  @ComponentTypeNameRef
  Integer getTypeName()
  {
    return null;
  }
}
