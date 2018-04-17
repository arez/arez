package com.example.observer_ref;

import arez.annotations.ArezComponent;
import arez.annotations.Autorun;
import arez.annotations.ObserverRef;

@ArezComponent
public abstract class BadReturnTypeModel
{
  @Autorun
  protected void doStuff()
  {
  }

  @ObserverRef
  abstract String getDoStuffObserver();
}
