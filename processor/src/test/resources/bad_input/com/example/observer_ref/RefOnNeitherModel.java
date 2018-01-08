package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.ObserverRef;

@ArezComponent
public class RefOnNeitherModel
{
  @ObserverRef
  Observer getRenderObserver()
  {
    throw new IllegalStateException();
  }
}
