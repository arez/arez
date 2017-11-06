package com.example.component_name;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ComponentTypeName;

@ArezComponent( nameIncludesId = false )
public class ComponentTypeNameAloneOnSingletonModel
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
}
