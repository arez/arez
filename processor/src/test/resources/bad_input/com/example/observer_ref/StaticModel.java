package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Observed;
import arez.annotations.ObserverRef;

@ArezComponent
public abstract class StaticModel
{
  @Observed
  protected void doStuff()
  {
  }

  @ObserverRef
  static Observer getDoStuffObserver()
  {
    throw new IllegalStateException();
  }
}
