package com.example.observe;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;

@ArezComponent
public abstract class ApplicationExecutorButNoOnDepsChangedModel
{
  @Observe( executor = Executor.APPLICATION )
  void doStuff()
  {
  }

  @ObserverRef
  abstract Observer getDoStuffObserver();
}
