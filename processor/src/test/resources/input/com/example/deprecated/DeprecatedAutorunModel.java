package com.example.deprecated;

import arez.annotations.ArezComponent;
import arez.annotations.Observed;

@ArezComponent
public abstract class DeprecatedAutorunModel
{
  @Deprecated
  @Observed
  protected void doStuff()
  {
  }
}
