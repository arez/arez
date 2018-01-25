package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.ObserverRef;

@ArezComponent
public abstract class RefOnNeitherModel
{
  @ObserverRef
  abstract Observer getRenderObserver();
}
