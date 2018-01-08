package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Autorun;
import arez.annotations.ObserverRef;

@ArezComponent
public class BadNameModel
{
  @Autorun
  public void doStuff()
  {
  }

  @ObserverRef( name = "-ace" )
  Observer getDoStuffObserver()
  {
    throw new IllegalStateException();
  }
}
