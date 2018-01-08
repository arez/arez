package com.example.component_name;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentTypeName;

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
