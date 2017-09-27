package com.example.component_name;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ComponentName;

@ArezComponent
public class ComponentNameModel
{
  @Action
  public void doStuff( final long time, float someOtherParameter )
  {
  }

  @ComponentName
  public String getComponentName()
  {
    return "";
  }
}
