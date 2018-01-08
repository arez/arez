package com.example.component_name;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentName;

@ArezComponent
public class ComponentNameMustReturnValueModel
{
  @Action
  void myAction()
  {
  }

  @ComponentName
  void getTypeName()
  {
  }
}
