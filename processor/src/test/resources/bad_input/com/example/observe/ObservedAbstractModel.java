package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;

@ArezComponent
public abstract class ObservedAbstractModel
{
  @Observe
  abstract void doStuff();
}
