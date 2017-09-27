package com.example.component_name;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ComponentName;

@ArezComponent
public class ComponentNameStaticModel
{
  @Action
  void myAction()
  {
  }

  @ComponentName
  static String getTypeName()
  {
    return null;
  }
}
