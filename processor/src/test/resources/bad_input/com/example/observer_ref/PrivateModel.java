package com.example.observer_ref;

import org.realityforge.arez.Observer;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Autorun;
import org.realityforge.arez.annotations.ObserverRef;

@ArezComponent
public class PrivateModel
{
  @Autorun
  public void doStuff()
  {
  }

  @ObserverRef
  private Observer getDoStuffObserver()
  {
    throw new IllegalStateException();
  }
}
