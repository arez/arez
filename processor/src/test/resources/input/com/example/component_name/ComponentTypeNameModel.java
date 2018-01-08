package com.example.component_name;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentName;
import arez.annotations.ComponentTypeName;

@ArezComponent
public class ComponentTypeNameModel
{
  @Action
  public void doStuff( final long time, float someOtherParameter )
  {
  }

  @ComponentTypeName
  public String getTypeName()
  {
    return "";
  }

  @ComponentName
  public String getComponentName()
  {
    return "";
  }
}
