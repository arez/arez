package com.example.observer_ref;

import org.realityforge.arez.Observer;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Autorun;
import org.realityforge.arez.annotations.ObserverRef;

@ArezComponent
public class RefOnAutorunModel
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
}
