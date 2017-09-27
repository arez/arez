package com.example.component_type_name;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ComponentTypeName;

@ArezComponent
public class ComponentTypeNamePrivateModel
{
  @Action
  void myAction()
  {
  }

  @ComponentTypeName
  private String getTypeName()
  {
    return null;
  }
}
