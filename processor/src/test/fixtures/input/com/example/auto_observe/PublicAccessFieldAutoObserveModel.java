package com.example.auto_observe;

import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;
import arez.component.ComponentObservable;

@ArezComponent
abstract class PublicAccessFieldAutoObserveModel
{
  @AutoObserve
  public final MyType _field = null;

  static final class MyType
    implements ComponentObservable
  {
    @Override
    public boolean observe()
    {
      return true;
    }
  }
}
