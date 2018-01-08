package com.example.component_name;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentName;

@ArezComponent
public class ComponentNamePrivateModel
{
  @Action
  void myAction()
  {
  }

  @ComponentName
  private String getTypeName()
  {
    return null;
  }
}
