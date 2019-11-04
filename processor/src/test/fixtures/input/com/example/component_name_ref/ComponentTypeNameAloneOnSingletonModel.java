package com.example.component_name_ref;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentTypeNameRef;

@ArezComponent( nameIncludesId = false )
public abstract class ComponentTypeNameAloneOnSingletonModel
{
  @Action
  public void doStuff( final long time, float someOtherParameter )
  {
  }

  @ComponentTypeNameRef
  public abstract String getTypeName();
}
