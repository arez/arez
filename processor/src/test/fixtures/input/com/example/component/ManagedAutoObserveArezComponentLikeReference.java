package com.example.component;

import arez.annotations.ArezComponentLike;
import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;

@ArezComponent
abstract class ManagedAutoObserveArezComponentLikeReference
{
  @AutoObserve( validateTypeAtRuntime = true )
  final MyComponent _myComponent = null;

  @ArezComponentLike
  public interface MyComponent
  {
  }
}
