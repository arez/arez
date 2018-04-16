package com.example.package_access.other;

import arez.Observer;
import arez.annotations.Autorun;
import arez.annotations.ObserverRef;

public abstract class BaseObserverRefModel
{
  @Autorun
  public void doStuff()
  {
  }

  @ObserverRef
  abstract Observer getDoStuffObserver();
}
