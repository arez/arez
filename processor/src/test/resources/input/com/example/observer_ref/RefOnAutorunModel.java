package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Autorun;
import arez.annotations.ObserverRef;

@ArezComponent
public class RefOnAutorunModel
{
  @Autorun
  public void doStuff()
  {
  }

  @ObserverRef
  Observer getDoStuffObserver()
  {
    throw new IllegalStateException();
  }
}
