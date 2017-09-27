package com.example.component_name;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ComponentName;

@ArezComponent
public class ComponentNameFinalModel
{
  @Action
  void myAction()
  {
  }

  @ComponentName
  final String getTypeName()
  {
    return null;
  }
}
