package com.example.observer_ref;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;

@ArezComponent
public abstract class BadReturnTypeModel
{
  @Observe
  void doStuff()
  {
  }

  @ObserverRef
  abstract String getDoStuffObserver();
}
