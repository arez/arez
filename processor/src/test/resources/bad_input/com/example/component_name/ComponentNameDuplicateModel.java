package com.example.component_name;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ComponentName;

@ArezComponent
public class ComponentNameDuplicateModel
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

  @ComponentName
  String getTypeName2()
  {
    return null;
  }
}
