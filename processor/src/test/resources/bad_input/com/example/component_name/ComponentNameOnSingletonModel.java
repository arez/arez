package com.example.component_name;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ComponentName;

@ArezComponent( singleton = true )
public class ComponentNameOnSingletonModel
{
  @Action
  void myAction()
  {
  }

  @ComponentName
  String getTypeName()
  {
    return null;
  }
}
