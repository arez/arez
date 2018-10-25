package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;

@ArezComponent
public abstract class NestedActionsAllowedObserveModel
{
  @Observe( nestedActionsAllowed = true )
  protected void doStuff()
  {
  }
}
