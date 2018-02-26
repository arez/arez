package com.example.component;

import arez.annotations.Action;
import arez.annotations.ArezComponent;

@ArezComponent( disposeOnDeactivate = true )
public abstract class DisposeOnDeactivateModel
{
  @Action
  public void doStuff( final long time, float someOtherParameter )
  {
  }
}
