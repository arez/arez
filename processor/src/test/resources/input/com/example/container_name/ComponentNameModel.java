package com.example.container_name;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.ContainerName;

@Container
public class ComponentNameModel
{
  @Action
  public void doStuff( final long time, float someOtherParameter )
  {
  }

  @ContainerName
  public String getComponentName()
  {
    return "";
  }
}
