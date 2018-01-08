package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Autorun;
import arez.annotations.ObserverRef;

@ArezComponent
public class ParametersModel
{
  @Autorun
  public void doStuff()
  {
  }

  @ObserverRef
  Observer getDoStuffObserver( int i )
  {
    throw new IllegalStateException();
  }
}
