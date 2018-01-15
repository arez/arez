package com.example.component_name;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentNameRef;

@ArezComponent
public class ComponentNameMustNotHaveParametersModel
{
  @Action
  void myAction()
  {
  }

  @ComponentNameRef
  String getTypeName( int i )
  {
    return null;
  }
}
