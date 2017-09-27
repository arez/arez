package com.example.component_type_name;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ComponentTypeName;

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
