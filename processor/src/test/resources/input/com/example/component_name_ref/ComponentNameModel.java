package com.example.component_name_ref;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentNameRef;

@ArezComponent
public abstract class ComponentNameModel
{
  @Action
  public void doStuff( final long time, float someOtherParameter )
  {
  }

  @ComponentNameRef
  public abstract String getComponentName();
}
