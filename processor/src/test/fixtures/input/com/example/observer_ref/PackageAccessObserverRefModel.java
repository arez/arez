package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;

@ArezComponent
abstract class PackageAccessObserverRefModel
{
  @Observe
  void doStuff()
  {
  }

  @ObserverRef
  abstract Observer getDoStuffObserver();
}
