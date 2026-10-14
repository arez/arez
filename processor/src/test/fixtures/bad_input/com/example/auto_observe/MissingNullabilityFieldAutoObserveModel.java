package com.example.auto_observe;

import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;
import arez.component.ComponentObservable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.NullUnmarked;

@NullUnmarked
@ArezComponent
abstract class MissingNullabilityFieldAutoObserveModel
{
  @AutoObserve
  final MyType _field = null;

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
