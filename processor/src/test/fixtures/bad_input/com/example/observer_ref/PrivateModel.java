package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;

@ArezComponent
public abstract class PrivateModel
{
  @Observe
  protected void doStuff()
  {
  }

  @ObserverRef
  private Observer getDoStuffObserver()
  {
    throw new IllegalStateException();
  }
}
