package com.example.auto_observe;

import arez.annotations.ArezComponentLike;
import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class RuntimeTypeValidateFieldAutoObserveModel
{
  @AutoObserve( validateTypeAtRuntime = true )
  @Nullable
  final MyType _field = null;

  @NullMarked
  @ArezComponentLike
  interface MyType
  {
  }
}
