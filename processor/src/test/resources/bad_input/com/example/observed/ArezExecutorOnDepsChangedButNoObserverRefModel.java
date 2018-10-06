package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.OnDepsChanged;

@ArezComponent
public abstract class ArezExecutorOnDepsChangedButNoObserverRefModel
{
  @Observe
  void doStuff()
  {
  }

  @OnDepsChanged
  public void onDoStuffDepsChanged()
  {
  }
}
