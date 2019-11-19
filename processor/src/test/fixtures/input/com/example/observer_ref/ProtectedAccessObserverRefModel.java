package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;

@ArezComponent
abstract class ProtectedAccessObserverRefModel
{
  @Observe
  protected void doStuff()
  {
  }

  @ObserverRef
  protected abstract Observer getDoStuffObserver();
}
