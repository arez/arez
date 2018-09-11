package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observed;

@ArezComponent
public abstract class ApplicationExecutorButNoOnDepsChangedModel
{
  @Observed( executor = Executor.APPLICATION )
  void doStuff()
  {
  }
}
