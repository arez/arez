package com.example.observe;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class ApplicationExecutorButNoOnDepsChangeModel
{
  @Observe( executor = Executor.EXTERNAL )
  void doStuff()
  {
  }

  @ObserverRef
  abstract @Nullable Observer getDoStuffObserver();
}
