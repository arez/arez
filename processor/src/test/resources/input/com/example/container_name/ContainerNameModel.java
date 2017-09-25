package com.example.container_name;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.ContainerName;
import org.realityforge.arez.annotations.ContainerNamePrefix;

@Container
public class ContainerNameModel
{
  @Action
  public void doStuff( final long time, float someOtherParameter )
  {
  }

  @ContainerNamePrefix
  public String getTypeName()
  {
    return "";
  }

  @ContainerName
  public String getComponentName()
  {
    return "";
  }
}
