package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class ProtectedAccessObserverRefModel
{
  @Observe
  void doStuff()
  {
  }

  @ObserverRef
  protected abstract @Nullable Observer getDoStuffObserver();
}
