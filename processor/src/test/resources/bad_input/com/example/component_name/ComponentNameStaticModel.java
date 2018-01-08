package com.example.component_name;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentName;

@ArezComponent
public class ComponentNameStaticModel
{
  @Action
  void myAction()
  {
  }

  @ComponentName
  static String getTypeName()
  {
    return null;
  }
}
