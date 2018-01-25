package com.example.observer_ref;

import arez.annotations.ArezComponent;
import arez.annotations.Autorun;
import arez.annotations.ObserverRef;

@ArezComponent
public abstract class VoidReturnModel
{
  @Autorun
  public void doStuff()
  {
  }

  @ObserverRef
  abstract void getDoStuffObserver();
}
