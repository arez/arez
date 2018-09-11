package com.example.observed;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observed;
import arez.annotations.ObserverRef;

@ArezComponent
public abstract class ApplicationExecutorButNoOnDepsChangedModel
{
  @Observed( executor = Executor.APPLICATION )
  void doStuff()
  {
  }

  @ObserverRef
  abstract Observer getDoStuffObserver();
}
