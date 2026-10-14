package com.example.auto_observe;

import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;
import arez.annotations.Observable;
import arez.component.ComponentObservable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class ConcreteObservableAutoObserveModel
{
  @AutoObserve
  @Observable
  @Nullable MyType getValue()
  {
    return null;
  }

  void setValue( final @Nullable MyType value )
  {
  }

  @NullMarked
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
