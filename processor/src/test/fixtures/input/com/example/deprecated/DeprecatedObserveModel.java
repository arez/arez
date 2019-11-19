package com.example.deprecated;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;

@ArezComponent
abstract class DeprecatedObserveModel
{
  @Deprecated
  @Observe
  protected void doStuff()
  {
  }
}
