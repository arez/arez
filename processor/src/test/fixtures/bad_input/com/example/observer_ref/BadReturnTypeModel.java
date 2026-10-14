package com.example.observer_ref;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class BadReturnTypeModel
{
  @Observe
  void doStuff()
  {
  }

  @ObserverRef
  abstract @Nullable String getDoStuffObserver();
}
