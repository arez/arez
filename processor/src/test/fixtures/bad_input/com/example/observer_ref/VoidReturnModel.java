package com.example.observer_ref;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;

@ArezComponent
public abstract class VoidReturnModel
{
  @Observe
  protected void doStuff()
  {
  }

  @ObserverRef
  abstract void getDoStuffObserver();
}
