package com.example.component;

import arez.annotations.ArezComponentLike;
import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class ManagedAutoObserveArezComponentLikeReference
{
  @AutoObserve( validateTypeAtRuntime = true )
  @Nullable
  final MyComponent _myComponent = null;

  @NullMarked
  @ArezComponentLike
  public interface MyComponent
  {
  }
}
