package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;

@ArezComponent
abstract class Suppressed1ProtectedAccessObserverRefModel
{
  @Observe
  void doStuff()
  {
  }

  // This uses the SOURCE retention suppression
  @SuppressWarnings( "Arez:ProtectedMethod" )
  @ObserverRef
  protected abstract Observer getDoStuffObserver();
}
