package com.example.component_name;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentTypeNameRef;

@ArezComponent( nameIncludesId = false )
public class ComponentTypeNameAloneOnSingletonModel
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
}
