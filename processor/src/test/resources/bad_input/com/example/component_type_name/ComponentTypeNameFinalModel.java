package com.example.component_type_name;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ComponentTypeName;

@ArezComponent
public class ComponentTypeNameFinalModel
{
  @Action
  void myAction()
  {
  }

  @ComponentTypeName
  final String getTypeName()
  {
    return null;
  }
}
