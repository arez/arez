package com.example.observer_ref;

import org.realityforge.arez.Observer;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Autorun;
import org.realityforge.arez.annotations.ObserverRef;

@ArezComponent
public class FinalModel
{
  @Autorun
  public void doStuff()
  {
  }

  @ObserverRef
  final Observer getDoStuffObserver()
  {
    throw new IllegalStateException();
  }
}
