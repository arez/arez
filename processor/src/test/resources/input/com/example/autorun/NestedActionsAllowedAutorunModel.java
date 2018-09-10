package com.example.autorun;

import arez.annotations.ArezComponent;
import arez.annotations.Autorun;

@ArezComponent
public abstract class NestedActionsAllowedAutorunModel
{
  @Autorun( nestedActionsAllowed = true )
  protected void doStuff()
  {
  }
}
