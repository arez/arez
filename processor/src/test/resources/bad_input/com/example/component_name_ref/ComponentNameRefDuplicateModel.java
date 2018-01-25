package com.example.component_name_ref;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentNameRef;

@ArezComponent
public abstract class ComponentNameRefDuplicateModel
{
  @Action
  void myAction()
  {
  }

  @ComponentNameRef
  abstract String getTypeName();

  @ComponentNameRef
  abstract String getTypeName2();
}
