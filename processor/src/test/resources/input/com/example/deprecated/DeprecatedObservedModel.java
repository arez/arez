package com.example.deprecated;

import arez.annotations.ArezComponent;
import arez.annotations.Observed;

@ArezComponent
public abstract class DeprecatedObservedModel
{
  @Deprecated
  @Observed
  protected void doStuff()
  {
  }
}
