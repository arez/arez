package com.example.observer_ref;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Autorun;
import org.realityforge.arez.annotations.ObserverRef;

@ArezComponent
public class BadReturnTypeModel
{
  @Autorun
  public void doStuff()
  {
  }

  @ObserverRef
  String getDoStuffObserver()
  {
    throw new IllegalStateException();
  }
}
