package com.example.component_name;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentNameRef;

@ArezComponent( nameIncludesId = false )
public class ComponentNameOnSingletonModel
{
  @Action
  void myAction()
  {
  }

  @ComponentNameRef
  String getTypeName()
  {
    return null;
  }
}
