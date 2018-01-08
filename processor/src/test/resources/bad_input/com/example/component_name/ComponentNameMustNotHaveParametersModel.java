package com.example.component_name;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentName;

@ArezComponent
public class ComponentNameMustNotHaveParametersModel
{
  @Action
  void myAction()
  {
  }

  @ComponentName
  String getTypeName( int i )
  {
    return null;
  }
}
