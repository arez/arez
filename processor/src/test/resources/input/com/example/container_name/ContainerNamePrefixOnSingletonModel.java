package com.example.container_name;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.ContainerNamePrefix;

@Container( singleton = true )
public class ContainerNamePrefixOnSingletonModel
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
}
