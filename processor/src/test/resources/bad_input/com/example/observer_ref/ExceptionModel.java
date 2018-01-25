package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Autorun;
import arez.annotations.ObserverRef;

@ArezComponent
public abstract class ExceptionModel
{
  @Autorun
  public void doStuff()
  {
  }

  @ObserverRef
  abstract Observer getDoStuffObserver()
    throws Exception;
}
