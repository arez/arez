package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;

@ArezComponent
public abstract class ExceptionModel
{
  @Observe
  protected void doStuff()
  {
  }

  @SuppressWarnings( { "RedundantThrows", "RedundantSuppression" } )
  @ObserverRef
  abstract Observer getDoStuffObserver()
    throws Exception;
}
