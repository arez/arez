package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;

@ArezComponent
public abstract class DuplicateNameModel
{
  @Observe
  protected void doStuff()
  {
  }

  @ObserverRef
  abstract Observer getDoStuffObserver();

  @ObserverRef( name = "doStuff" )
  abstract Observer getDoStuffObserver2();
}
