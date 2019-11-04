package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;

@ArezComponent
public abstract class ObserveLowerPriorityObserveModel
{
  @Observe( observeLowerPriorityDependencies = true )
  protected void doStuff()
  {
  }
}
