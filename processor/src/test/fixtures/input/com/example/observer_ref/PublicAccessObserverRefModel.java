package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;

@ArezComponent
abstract class PublicAccessObserverRefModel
{
  @Observe
  protected void doStuff()
  {
  }

  @ObserverRef
  public abstract Observer getDoStuffObserver();
}
