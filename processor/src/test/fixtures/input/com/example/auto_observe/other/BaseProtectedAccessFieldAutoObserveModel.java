package com.example.auto_observe.other;

import arez.annotations.AutoObserve;
import arez.component.ComponentObservable;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class BaseProtectedAccessFieldAutoObserveModel
{
  @AutoObserve
  @Nullable
  protected final MyType _field = null;

  @NullMarked
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
