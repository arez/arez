package com.example.component_name;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentName;

@ArezComponent( nameIncludesId = false )
public class ComponentNameOnSingletonModel
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
}
