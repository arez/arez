package com.example.observer_ref;

import arez.annotations.ArezComponent;
import arez.annotations.Autorun;
import arez.annotations.ObserverRef;

@ArezComponent
public class BadReturnTypeModel
{
  @Autorun
  public void doStuff()
  {
  }

  @ObserverRef
  String getDoStuffObserver()
  {
    throw new IllegalStateException();
  }
}
