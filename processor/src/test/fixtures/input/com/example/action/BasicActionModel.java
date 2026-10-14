package com.example.action;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class BasicActionModel
{
  @Action
  public void doStuff( final long time, float someOtherParameter )
  {
  }
}
