package com.example.deprecated;

import arez.annotations.Action;
import arez.annotations.ArezComponent;

@ArezComponent
public class DeprecatedActionModel
{
  @Deprecated
  @Action
  public void doStuff( final long time, float someOtherParameter )
  {
  }
}
