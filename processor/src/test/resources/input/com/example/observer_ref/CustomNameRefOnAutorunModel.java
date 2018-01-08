package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Autorun;
import arez.annotations.ObserverRef;

@ArezComponent
public class CustomNameRefOnAutorunModel
{
  @Autorun
  public void doStuff()
  {
  }

  @ObserverRef( name = "doStuff" )
  Observer observer()
  {
    throw new IllegalStateException();
  }
}
