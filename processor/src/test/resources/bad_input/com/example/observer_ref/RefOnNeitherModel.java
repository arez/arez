package com.example.observer_ref;

import org.realityforge.arez.Observer;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ObserverRef;

@ArezComponent
public class RefOnNeitherModel
{
  @ObserverRef
  Observer getRenderObserver()
  {
    throw new IllegalStateException();
  }
}
