package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Observed;

@ArezComponent
public abstract class ObservedPrivateModel
{
  @Observed
  private void doStuff()
  {
  }
}
