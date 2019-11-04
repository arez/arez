package com.example.deprecated;

import arez.annotations.Action;
import arez.annotations.ArezComponent;

@ArezComponent
public abstract class DeprecatedActionModel
{
  @Deprecated
  @Action
  public void doStuff( final long time, float someOtherParameter )
  {
  }
}
