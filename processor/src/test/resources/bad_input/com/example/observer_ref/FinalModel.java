package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;

@ArezComponent
public abstract class FinalModel
{
  @Observe
  protected void doStuff()
  {
  }

  @ObserverRef
  final Observer getDoStuffObserver()
  {
    throw new IllegalStateException();
  }
}
