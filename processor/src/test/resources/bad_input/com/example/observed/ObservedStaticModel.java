package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;

@ArezComponent
public abstract class ObservedStaticModel
{
  @Observe
  static void doStuff()
  {
  }
}
