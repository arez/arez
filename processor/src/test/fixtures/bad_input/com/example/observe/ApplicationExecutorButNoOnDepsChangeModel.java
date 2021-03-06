package com.example.observe;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;

@ArezComponent
public abstract class ApplicationExecutorButNoOnDepsChangeModel
{
  @Observe( executor = Executor.EXTERNAL )
  void doStuff()
  {
  }

  @ObserverRef
  abstract Observer getDoStuffObserver();
}
