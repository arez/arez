package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Observed;
import arez.annotations.ObserverRef;

@ArezComponent
public abstract class BadNameModel
{
  @Observed
  protected void doStuff()
  {
  }

  @ObserverRef( name = "-ace" )
  abstract Observer getDoStuffObserver();
}
