package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;

@ArezComponent
public abstract class BadNameModel2
{
  @Observe
  void doStuff()
  {
  }

  @ObserverRef( name = "int" )
  abstract Observer getDoStuffObserver();
}
