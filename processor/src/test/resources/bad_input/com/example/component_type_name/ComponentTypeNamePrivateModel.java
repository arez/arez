package com.example.component_type_name;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentTypeName;

@ArezComponent
public class ComponentTypeNamePrivateModel
{
  @Action
  void myAction()
  {
  }

  @ComponentTypeName
  private String getTypeName()
  {
    return null;
  }
}
