package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Observed;
import arez.annotations.ObserverRef;

@ArezComponent
public abstract class CustomNameRefOnAutorunModel
{
  @Observed
  protected void doStuff()
  {
  }

  @ObserverRef( name = "doStuff" )
  abstract Observer observer();
}
