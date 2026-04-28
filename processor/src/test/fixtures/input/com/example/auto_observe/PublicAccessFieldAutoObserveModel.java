package com.example.auto_observe;

import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;
import arez.component.ComponentObservable;
import javax.annotation.Nullable;

@ArezComponent
abstract class PublicAccessFieldAutoObserveModel
{
  @AutoObserve
  @Nullable
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
