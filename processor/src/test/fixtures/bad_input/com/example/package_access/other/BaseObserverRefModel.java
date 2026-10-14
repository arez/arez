package com.example.package_access.other;

import arez.Observer;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class BaseObserverRefModel
{
  @Observe
  protected void doStuff()
  {
  }

  @ObserverRef
  abstract @Nullable Observer getDoStuffObserver();
}
