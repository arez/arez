package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class PackageAccessObserverRefModel
{
  @Observe
  void doStuff()
  {
  }

  @ObserverRef
  abstract @Nullable Observer getDoStuffObserver();
}
