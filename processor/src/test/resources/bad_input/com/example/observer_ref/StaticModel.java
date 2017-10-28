package com.example.observer_ref;

import org.realityforge.arez.Observer;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Autorun;
import org.realityforge.arez.annotations.ObserverRef;

@ArezComponent
public class StaticModel
{
  @Autorun
  public void doStuff()
  {
  }

  @ObserverRef
  static Observer getDoStuffObserver()
  {
    throw new IllegalStateException();
  }
}
