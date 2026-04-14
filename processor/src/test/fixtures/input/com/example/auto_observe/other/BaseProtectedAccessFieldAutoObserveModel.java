package com.example.auto_observe.other;

import arez.annotations.AutoObserve;
import arez.component.ComponentObservable;

public abstract class BaseProtectedAccessFieldAutoObserveModel
{
  @AutoObserve
  protected final MyType _field = null;

  public static final class MyType
    implements ComponentObservable
  {
    @Override
    public boolean observe()
    {
      return true;
    }
  }
}
