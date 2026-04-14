package com.example.auto_observe;

import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;
import arez.component.ComponentObservable;
import javax.annotation.Nullable;

@ArezComponent
abstract class NullableMethodAutoObserveModel
{
  @AutoObserve
  @Nullable
  MyType current()
  {
    return null;
  }

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
