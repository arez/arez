package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Observed;
import arez.annotations.OnDepsChanged;

@ArezComponent
public abstract class ArezExecutorOnDepsChangedButNoObserverRefModel
{
  @Observed
  void doStuff()
  {
  }

  @OnDepsChanged
  public void onDoStuffDepsChanged()
  {
  }
}
