package com.example.auto_observe;

import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;
import arez.component.ComponentObservable;
import javax.annotation.Nullable;

@ArezComponent
abstract class NullableFieldAutoObserveModel
{
  @AutoObserve
  @Nullable
  final MyType _field = null;

  static class MyType
    implements ComponentObservable
  {
    @Override
    public boolean observe()
    {
      return true;
    }
  }
}
