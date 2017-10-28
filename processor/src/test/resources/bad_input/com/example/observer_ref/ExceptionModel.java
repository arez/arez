package com.example.observer_ref;

import org.realityforge.arez.Observer;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Autorun;
import org.realityforge.arez.annotations.ObserverRef;

@ArezComponent
public class ExceptionModel
{
  @Autorun
  public void doStuff()
  {
  }

  @ObserverRef
  Observer getDoStuffObserver()
    throws Exception
  {
    throw new IllegalStateException();
  }
}
