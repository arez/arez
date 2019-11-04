package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;

@ArezComponent
public abstract class CustomNameRefOnObserveModel1
{
  @Observe
  protected void doStuff()
  {
  }

  @ObserverRef( name = "doStuff" )
  abstract Observer observer();
}
