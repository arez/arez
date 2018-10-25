package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;

@ArezComponent
public abstract class ObserveAbstractModel
{
  @Observe
  abstract void doStuff();
}
