package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class NestedActionsAllowedObserveModel
{
  @Observe( nestedActionsAllowed = true )
  void doStuff()
  {
  }
}
