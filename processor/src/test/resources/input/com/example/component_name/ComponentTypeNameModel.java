package com.example.component_name;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentNameRef;
import arez.annotations.ComponentTypeNameRef;

@ArezComponent
public class ComponentTypeNameModel
{
  @Action
  public void doStuff( final long time, float someOtherParameter )
  {
  }

  @ComponentTypeNameRef
  public String getTypeName()
  {
    return "";
  }

  @ComponentNameRef
  public String getComponentName()
  {
    return "";
  }
}
