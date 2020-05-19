package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;
import arez.annotations.SuppressArezWarnings;

@ArezComponent
abstract class Suppressed2PublicAccessObserverRefModel
{
  @Observe
  void doStuff()
  {
  }

  // This uses the CLASS retention suppression
  @SuppressArezWarnings( "Arez:PublicRefMethod" )
  @ObserverRef
  public abstract Observer getDoStuffObserver();
}
