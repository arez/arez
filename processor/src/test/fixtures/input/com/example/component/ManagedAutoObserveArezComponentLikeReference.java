package com.example.component;

import arez.annotations.ArezComponentLike;
import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;
import javax.annotation.Nullable;

@ArezComponent
abstract class ManagedAutoObserveArezComponentLikeReference
{
  @AutoObserve( validateTypeAtRuntime = true )
  @Nullable
  final MyComponent _myComponent = null;

  @ArezComponentLike
  public interface MyComponent
  {
  }
}
