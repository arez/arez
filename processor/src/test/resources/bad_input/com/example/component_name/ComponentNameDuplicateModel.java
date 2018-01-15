package com.example.component_name;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentNameRef;

@ArezComponent
public class ComponentNameDuplicateModel
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

  @ComponentNameRef
  String getTypeName2()
  {
    return null;
  }
}
