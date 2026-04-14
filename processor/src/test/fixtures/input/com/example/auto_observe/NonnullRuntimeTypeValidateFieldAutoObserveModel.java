package com.example.auto_observe;

import arez.annotations.ActAsComponent;
import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;
import javax.annotation.Nonnull;

@ArezComponent
abstract class NonnullRuntimeTypeValidateFieldAutoObserveModel
{
  @AutoObserve( validateTypeAtRuntime = true )
  @Nonnull
  final MyType _field = new MyTypeImpl();

  @ActAsComponent
  interface MyType
  {
  }

  static final class MyTypeImpl
    implements MyType
  {
  }
}
