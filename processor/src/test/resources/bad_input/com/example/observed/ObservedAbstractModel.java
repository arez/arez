package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;

@ArezComponent
public abstract class ObservedAbstractModel
{
  @Observe
  abstract void doStuff();
}
