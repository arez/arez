package com.example.component_type_name;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentTypeNameRef;

@ArezComponent
public abstract class ComponentTypeNameDuplicateModel
{
  @Action
  void myAction()
  {
  }

  @ComponentTypeNameRef
  abstract String getTypeName();

  @ComponentTypeNameRef
  abstract String getTypeName2();
}
