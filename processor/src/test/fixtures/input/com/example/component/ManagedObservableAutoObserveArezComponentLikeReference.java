package com.example.component;

import arez.annotations.ArezComponentLike;
import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;
import arez.annotations.Observable;

@ArezComponent
abstract class ManagedObservableAutoObserveArezComponentLikeReference
{
  @AutoObserve( validateTypeAtRuntime = true )
  @Observable
  abstract MyComponent getMyComponent();

  abstract void setMyComponent( MyComponent component );

  @ArezComponentLike
  public interface MyComponent
  {
  }
}
