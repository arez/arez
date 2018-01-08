package com.example.component_type_name;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentTypeName;

@ArezComponent
public class ComponentTypeNameMustReturnValueModel
{
  @Action
  void myAction()
  {
  }

  @ComponentTypeName
  void getTypeName()
  {
  }
}
