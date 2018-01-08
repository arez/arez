package com.example.component_type_name;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentTypeName;

@ArezComponent
public class ComponentTypeNameStaticModel
{
  @Action
  void myAction()
  {
  }

  @ComponentTypeName
  static String getTypeName()
  {
    return null;
  }
}
