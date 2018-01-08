package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Autorun;
import arez.annotations.ObserverRef;

@ArezComponent
public class FinalModel
{
  @Autorun
  public void doStuff()
  {
  }

  @ObserverRef
  final Observer getDoStuffObserver()
  {
    throw new IllegalStateException();
  }
}
