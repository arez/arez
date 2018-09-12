package com.example.package_access.other;

import arez.Observer;
import arez.annotations.Observed;
import arez.annotations.ObserverRef;

public abstract class BaseObserverRefModel
{
  @Observed
  protected void doStuff()
  {
  }

  @ObserverRef
  abstract Observer getDoStuffObserver();
}
