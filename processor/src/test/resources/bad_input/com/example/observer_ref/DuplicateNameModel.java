package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Observed;
import arez.annotations.ObserverRef;

@ArezComponent
public abstract class DuplicateNameModel
{
  @Observed
  protected void doStuff()
  {
  }

  @ObserverRef
  abstract Observer getDoStuffObserver();

  @ObserverRef( name = "doStuff" )
  abstract Observer getDoStuffObserver2();
}
