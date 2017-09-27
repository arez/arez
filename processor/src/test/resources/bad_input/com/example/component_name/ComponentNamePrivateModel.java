package com.example.component_name;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ComponentName;

@ArezComponent
public class ComponentNamePrivateModel
{
  @Action
  void myAction()
  {
  }

  @ComponentName
  private String getTypeName()
  {
    return null;
  }
}
