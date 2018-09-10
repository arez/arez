package com.example.observer_ref;

import arez.annotations.ArezComponent;
import arez.annotations.Observed;
import arez.annotations.ObserverRef;

@ArezComponent
public abstract class VoidReturnModel
{
  @Observed
  protected void doStuff()
  {
  }

  @ObserverRef
  abstract void getDoStuffObserver();
}
