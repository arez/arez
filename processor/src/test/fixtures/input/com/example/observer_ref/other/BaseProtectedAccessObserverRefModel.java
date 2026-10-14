package com.example.observer_ref.other;

import arez.Observer;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class BaseProtectedAccessObserverRefModel
{
  @Observe
  protected void doStuff()
  {
  }

  @ObserverRef
  protected abstract @Nullable Observer getDoStuffObserver();
}
