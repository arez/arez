package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Autorun;
import arez.annotations.ObserverRef;

@ArezComponent
public abstract class StaticModel
{
  @Autorun
  public void doStuff()
  {
  }

  @ObserverRef
  static Observer getDoStuffObserver()
  {
    throw new IllegalStateException();
  }
}
