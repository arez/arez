package com.example.component_name;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentNameRef;

@ArezComponent
public class ComponentNameMustReturnValueModel
{
  @Action
  void myAction()
  {
  }

  @ComponentNameRef
  void getTypeName()
  {
  }
}
