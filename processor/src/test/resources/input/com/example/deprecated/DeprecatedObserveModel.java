package com.example.deprecated;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;

@ArezComponent
public abstract class DeprecatedObserveModel
{
  @Deprecated
  @Observe
  protected void doStuff()
  {
  }
}
