package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;

@ArezComponent
public abstract class NestedActionsAllowedObservedModel
{
  @Observe( nestedActionsAllowed = true )
  protected void doStuff()
  {
  }
}
