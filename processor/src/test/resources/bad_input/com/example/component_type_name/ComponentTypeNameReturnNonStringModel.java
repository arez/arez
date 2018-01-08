package com.example.component_type_name;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentTypeName;

@ArezComponent
public class ComponentTypeNameReturnNonStringModel
{
  @Action
  void myAction()
  {
  }

  @ComponentTypeName
  Integer getTypeName()
  {
    return null;
  }
}
