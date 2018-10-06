package com.example.deprecated;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;

@ArezComponent
public abstract class DeprecatedObservedModel
{
  @Deprecated
  @Observe
  protected void doStuff()
  {
  }
}
