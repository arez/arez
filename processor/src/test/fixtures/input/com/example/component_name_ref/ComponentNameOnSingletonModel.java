package com.example.component_name_ref;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentNameRef;

@ArezComponent( nameIncludesId = false )
public abstract class ComponentNameOnSingletonModel
{
  @Action
  void myAction()
  {
  }

  @ComponentNameRef
  abstract String getTypeName();
}
