package com.example.observer_ref;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Autorun;
import org.realityforge.arez.annotations.ObserverRef;

@ArezComponent
public class VoidReturnModel
{
  @Autorun
  public void doStuff()
  {
  }

  @ObserverRef
  void getDoStuffObserver()
  {
    throw new IllegalStateException();
  }
}
