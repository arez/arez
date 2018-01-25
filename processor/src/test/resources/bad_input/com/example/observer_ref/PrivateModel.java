package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Autorun;
import arez.annotations.ObserverRef;

@ArezComponent
public abstract class PrivateModel
{
  @Autorun
  public void doStuff()
  {
  }

  @ObserverRef
  private Observer getDoStuffObserver()
  {
    throw new IllegalStateException();
  }
}
