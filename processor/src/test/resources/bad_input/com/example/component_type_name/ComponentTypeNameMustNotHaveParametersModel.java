package com.example.component_type_name;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentTypeName;

@ArezComponent
public class ComponentTypeNameMustNotHaveParametersModel
{
  @Action
  void myAction()
  {
  }

  @ComponentTypeName
  String getTypeName( int i )
  {
    return null;
  }
}
