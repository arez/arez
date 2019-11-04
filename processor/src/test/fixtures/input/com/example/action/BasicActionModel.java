package com.example.action;

import arez.annotations.Action;
import arez.annotations.ArezComponent;

@ArezComponent
public abstract class BasicActionModel
{
  @Action
  public void doStuff( final long time, float someOtherParameter )
  {
  }
}
