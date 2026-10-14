package com.example.observer_ref;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class VoidReturnModel
{
  @Observe
  void doStuff()
  {
  }

  @ObserverRef
  abstract void getDoStuffObserver();
}
