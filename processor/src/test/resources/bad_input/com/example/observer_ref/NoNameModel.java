package com.example.observer_ref;

import org.realityforge.arez.Observer;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Autorun;
import org.realityforge.arez.annotations.ObserverRef;

@ArezComponent
public class NoNameModel
{
  @Autorun
  public void doStuff()
  {
  }

  @ObserverRef
  Observer observer()
  {
    throw new IllegalStateException();
  }
}
