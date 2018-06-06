package com.example.component;

import arez.annotations.Action;
import arez.annotations.ArezComponent;

@ArezComponent( observable = false )
public abstract class NotObservableModel
{
  @Action
  public void doStuff( final long time, float someOtherParameter )
  {
  }
}
