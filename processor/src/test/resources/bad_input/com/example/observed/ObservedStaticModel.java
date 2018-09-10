package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Observed;

@ArezComponent
public abstract class ObservedStaticModel
{
  @Observed
  static void doStuff()
  {
  }
}
