package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;

@ArezComponent
abstract class NestedActionsAllowedObserveModel
{
  @Observe( nestedActionsAllowed = true )
  void doStuff()
  {
  }
}
