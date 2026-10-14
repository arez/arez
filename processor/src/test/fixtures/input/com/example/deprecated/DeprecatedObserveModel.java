package com.example.deprecated;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class DeprecatedObserveModel
{
  @Deprecated
  @Observe
  void doStuff()
  {
  }
}
