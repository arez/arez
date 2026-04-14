package com.example.auto_observe;

import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;
import arez.component.ComponentObservable;
import javax.annotation.Nonnull;

@ArezComponent
abstract class NonnullFieldAutoObserveModel
{
  @AutoObserve
  @Nonnull
  final MyType _field = new MyType();

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
