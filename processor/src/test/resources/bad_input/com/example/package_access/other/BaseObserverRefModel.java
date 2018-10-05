package com.example.package_access.other;

import arez.Observer;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;

public abstract class BaseObserverRefModel
{
  @Observe
  protected void doStuff()
  {
  }

  @ObserverRef
  abstract Observer getDoStuffObserver();
}
