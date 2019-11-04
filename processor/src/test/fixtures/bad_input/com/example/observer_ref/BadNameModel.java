package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;

@ArezComponent
public abstract class BadNameModel
{
  @Observe
  protected void doStuff()
  {
  }

  @ObserverRef( name = "-ace" )
  abstract Observer getDoStuffObserver();
}
