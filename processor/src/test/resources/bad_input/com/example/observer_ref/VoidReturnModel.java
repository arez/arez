package com.example.observer_ref;

import arez.annotations.ArezComponent;
import arez.annotations.Autorun;
import arez.annotations.ObserverRef;

@ArezComponent
public class VoidReturnModel
{
  @Autorun
  public void doStuff()
  {
  }

  @ObserverRef
  void getDoStuffObserver()
  {
    throw new IllegalStateException();
  }
}
