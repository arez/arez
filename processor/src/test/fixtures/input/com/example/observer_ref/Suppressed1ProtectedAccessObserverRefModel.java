package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;

@ArezComponent
abstract class Suppressed1ProtectedAccessObserverRefModel
{
  @Observe
  protected void doStuff()
  {
  }

  // This uses the SOURCE retention suppression
  @SuppressWarnings( "Arez:ProtectedRefMethod" )
  @ObserverRef
  protected abstract Observer getDoStuffObserver();
}
