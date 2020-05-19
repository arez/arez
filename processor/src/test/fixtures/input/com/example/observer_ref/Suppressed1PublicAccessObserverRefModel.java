package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;

@ArezComponent
abstract class Suppressed1PublicAccessObserverRefModel
{
  @Observe
  void doStuff()
  {
  }

  // This uses the SOURCE retention suppression
  @SuppressWarnings( "Arez:PublicRefMethod" )
  @ObserverRef
  public abstract Observer getDoStuffObserver();
}
