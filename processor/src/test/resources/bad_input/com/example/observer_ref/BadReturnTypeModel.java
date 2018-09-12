package com.example.observer_ref;

import arez.annotations.ArezComponent;
import arez.annotations.Observed;
import arez.annotations.ObserverRef;

@ArezComponent
public abstract class BadReturnTypeModel
{
  @Observed
  protected void doStuff()
  {
  }

  @ObserverRef
  abstract String getDoStuffObserver();
}
