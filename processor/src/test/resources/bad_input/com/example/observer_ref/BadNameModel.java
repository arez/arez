package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Autorun;
import arez.annotations.ObserverRef;

@ArezComponent
public abstract class BadNameModel
{
  @Autorun
  protected void doStuff()
  {
  }

  @ObserverRef( name = "-ace" )
  abstract Observer getDoStuffObserver();
}
