package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class FinalModel
{
  @Observe
  void doStuff()
  {
  }

  @ObserverRef
  final @Nullable Observer getDoStuffObserver()
  {
    throw new IllegalStateException();
  }
}
