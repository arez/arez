package com.example.autorun;

import arez.annotations.ArezComponent;
import arez.annotations.Autorun;

@ArezComponent
public abstract class CanNestActionsAutorunModel
{
  @Autorun( canNestActions = true )
  protected void doStuff()
  {
  }
}
