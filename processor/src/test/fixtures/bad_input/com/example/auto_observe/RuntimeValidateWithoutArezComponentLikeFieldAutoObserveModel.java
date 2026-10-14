package com.example.auto_observe;

import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class RuntimeValidateWithoutArezComponentLikeFieldAutoObserveModel
{
  @AutoObserve( validateTypeAtRuntime = true )
  final @Nullable Object _field = null;
}
