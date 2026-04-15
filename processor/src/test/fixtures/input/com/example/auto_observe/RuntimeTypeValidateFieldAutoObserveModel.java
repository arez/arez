package com.example.auto_observe;

import arez.annotations.ArezComponentLike;
import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;

@ArezComponent
abstract class RuntimeTypeValidateFieldAutoObserveModel
{
  @AutoObserve( validateTypeAtRuntime = true )
  final MyType _field = null;

  @ArezComponentLike
  interface MyType
  {
  }
}
