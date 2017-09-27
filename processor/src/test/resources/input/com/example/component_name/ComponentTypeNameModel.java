package com.example.component_name;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ComponentName;
import org.realityforge.arez.annotations.ComponentTypeName;

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
