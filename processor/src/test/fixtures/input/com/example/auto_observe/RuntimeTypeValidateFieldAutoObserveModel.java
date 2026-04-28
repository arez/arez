package com.example.auto_observe;

import arez.annotations.ArezComponentLike;
import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;
import javax.annotation.Nullable;

@ArezComponent
abstract class RuntimeTypeValidateFieldAutoObserveModel
{
  @AutoObserve( validateTypeAtRuntime = true )
  @Nullable
  final MyType _field = null;

  @ArezComponentLike
  interface MyType
  {
  }
}
