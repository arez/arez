package com.example.component_name_ref;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentNameRef;
import arez.annotations.ComponentTypeNameRef;

@ArezComponent
public abstract class ComponentTypeNameModel
{
  @Action
  public void doStuff( final long time, float someOtherParameter )
  {
  }

  @ComponentTypeNameRef
  public abstract String getTypeName();

  @ComponentNameRef
  public abstract String getComponentName();
}
