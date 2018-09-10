package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Observed;

@ArezComponent
public abstract class ObserveLowerPriorityAutorunModel
{
  @Observed( observeLowerPriorityDependencies = true )
  protected void doStuff()
  {
  }
}
