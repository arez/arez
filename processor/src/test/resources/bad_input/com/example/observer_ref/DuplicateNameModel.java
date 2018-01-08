package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Autorun;
import arez.annotations.ObserverRef;

@ArezComponent
public class DuplicateNameModel
{
  @Autorun
  public void doStuff()
  {
  }

  @ObserverRef
  Observer getDoStuffObserver()
  {
    throw new IllegalStateException();
  }

  @ObserverRef( name = "doStuff" )
  Observer getDoStuffObserver2()
  {
    throw new IllegalStateException();
  }
}
