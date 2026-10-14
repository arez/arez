package com.example.component;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( disposeOnDeactivate = true )
abstract class DisposeOnDeactivateModel
{
  @Action
  public void doStuff( final long time, float someOtherParameter )
  {
  }
}
