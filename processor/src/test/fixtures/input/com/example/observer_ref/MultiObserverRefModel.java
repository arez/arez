package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;

@ArezComponent
abstract class MultiObserverRefModel
{
  @Observe
  protected void doStuff()
  {
  }

  @ObserverRef( name = "doStuff" )
  abstract Observer obs1();

  @ObserverRef( name = "doStuff" )
  abstract Observer obs2();
}
