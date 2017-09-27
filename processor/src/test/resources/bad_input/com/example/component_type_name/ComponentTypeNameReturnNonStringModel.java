package com.example.component_type_name;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ComponentTypeName;

@ArezComponent
public class ComponentTypeNameReturnNonStringModel
{
  @Action
  void myAction()
  {
  }

  @ComponentTypeName
  Integer getTypeName()
  {
    return null;
  }
}
