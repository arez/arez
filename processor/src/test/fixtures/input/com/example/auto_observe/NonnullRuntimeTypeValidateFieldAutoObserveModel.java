package com.example.auto_observe;

import arez.annotations.ArezComponentLike;
import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class NonnullRuntimeTypeValidateFieldAutoObserveModel
{
  @AutoObserve( validateTypeAtRuntime = true )
  @NonNull
  final MyType _field = new MyTypeImpl();

  @NullMarked
  @ArezComponentLike
  interface MyType
  {
  }

  @NullMarked
  static final class MyTypeImpl
    implements MyType
  {
  }
}
