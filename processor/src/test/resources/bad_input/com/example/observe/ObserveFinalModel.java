package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;

@ArezComponent
public abstract class ObserveFinalModel
{
  @Observe
  final void doStuff()
  {
  }
}
