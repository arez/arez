package com.example.component_name;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ComponentName;

@ArezComponent
public class ComponentNameMustReturnValueModel
{
  @Action
  void myAction()
  {
  }

  @ComponentName
  void getTypeName()
  {
  }
}
