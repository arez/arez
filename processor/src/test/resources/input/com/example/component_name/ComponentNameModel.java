package com.example.component_name;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentNameRef;

@ArezComponent
public class ComponentNameModel
{
  @Action
  public void doStuff( final long time, float someOtherParameter )
  {
  }

  @ComponentNameRef
  public String getComponentName()
  {
    return "";
  }
}
