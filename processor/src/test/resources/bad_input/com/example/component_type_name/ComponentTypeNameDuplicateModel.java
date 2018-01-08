package com.example.component_type_name;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentTypeName;

@ArezComponent
public class ComponentTypeNameDuplicateModel
{
  @Action
  void myAction()
  {
  }

  @ComponentTypeName
  String getTypeName()
  {
    return null;
  }

  @ComponentTypeName
  String getTypeName2()
  {
    return null;
  }
}
