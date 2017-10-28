package com.example.observer_ref;

import org.realityforge.arez.Observer;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Autorun;
import org.realityforge.arez.annotations.ObserverRef;

@ArezComponent
public class CustomNameRefOnAutorunModel
{
  @Autorun
  public void doStuff()
  {
  }

  @ObserverRef( name = "doStuff" )
  Observer observer()
  {
    throw new IllegalStateException();
  }
}
