package com.example.autorun;

import arez.annotations.ArezComponent;
import arez.annotations.Autorun;

@ArezComponent
public abstract class ObserveLowerPriorityAutorunModel
{
  @Autorun( observeLowerPriorityDependencies = true )
  protected void doStuff()
  {
  }
}
