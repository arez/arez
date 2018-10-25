package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;

@ArezComponent
public abstract class ObservedFinalModel
{
  @Observe
  final void doStuff()
  {
  }
}
