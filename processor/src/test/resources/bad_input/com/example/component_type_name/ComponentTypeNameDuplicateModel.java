package com.example.component_type_name;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentTypeNameRef;

@ArezComponent
public class ComponentTypeNameDuplicateModel
{
  @Action
  void myAction()
  {
  }

  @ComponentTypeNameRef
  String getTypeName()
  {
    return null;
  }

  @ComponentTypeNameRef
  String getTypeName2()
  {
    return null;
  }
}
