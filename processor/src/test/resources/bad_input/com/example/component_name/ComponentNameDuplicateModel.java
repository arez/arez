package com.example.component_name;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentName;

@ArezComponent
public class ComponentNameDuplicateModel
{
  @Action
  void myAction()
  {
  }

  @ComponentName
  String getTypeName()
  {
    return null;
  }

  @ComponentName
  String getTypeName2()
  {
    return null;
  }
}
