package com.example.auto_observe;

import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;
import arez.component.ComponentObservable;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class NonnullFieldAutoObserveModel
{
  @AutoObserve
  @NonNull
  final MyType _field = new MyType();

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
