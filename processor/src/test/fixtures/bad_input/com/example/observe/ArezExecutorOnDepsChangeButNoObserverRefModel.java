package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;

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
