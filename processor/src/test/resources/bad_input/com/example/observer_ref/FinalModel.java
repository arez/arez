package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Observed;
import arez.annotations.ObserverRef;

@ArezComponent
public abstract class FinalModel
{
  @Observed
  protected void doStuff()
  {
  }

  @ObserverRef
  final Observer getDoStuffObserver()
  {
    throw new IllegalStateException();
  }
}
