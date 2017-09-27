package com.example.component_name;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ComponentName;

@ArezComponent
public class ComponentNameMustNotHaveParametersModel
{
  @Action
  void myAction()
  {
  }

  @ComponentName
  String getTypeName( int i )
  {
    return null;
  }
}
