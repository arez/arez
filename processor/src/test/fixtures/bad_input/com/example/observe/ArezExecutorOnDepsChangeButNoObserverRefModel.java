package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class ArezExecutorOnDepsChangeButNoObserverRefModel
{
  @Observe
  void doStuff()
  {
  }

  @OnDepsChange
  void onDoStuffDepsChange()
  {
  }
}
