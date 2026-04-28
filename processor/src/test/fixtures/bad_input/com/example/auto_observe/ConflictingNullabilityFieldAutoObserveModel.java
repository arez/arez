package com.example.auto_observe;

import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;
import arez.component.ComponentObservable;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@ArezComponent
abstract class ConflictingNullabilityFieldAutoObserveModel
{
  @AutoObserve
  @Nullable
  @Nonnull
  final MyType _field = null;

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
