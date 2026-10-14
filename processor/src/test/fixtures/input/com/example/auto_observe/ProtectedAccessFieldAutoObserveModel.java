package com.example.auto_observe;

import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;
import arez.component.ComponentObservable;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class ProtectedAccessFieldAutoObserveModel
{
  @AutoObserve
  @Nullable
  protected final MyType _field = null;

  @NullMarked
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
