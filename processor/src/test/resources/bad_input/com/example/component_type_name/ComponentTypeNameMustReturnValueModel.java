package com.example.component_type_name;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ComponentTypeName;

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
