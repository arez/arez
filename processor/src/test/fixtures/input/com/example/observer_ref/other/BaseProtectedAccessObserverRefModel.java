package com.example.observer_ref.other;

import arez.Observer;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;

public abstract class BaseProtectedAccessObserverRefModel
{
  @Observe
  protected void doStuff()
  {
  }

  @ObserverRef
  protected abstract Observer getDoStuffObserver();
}
