package com.example.component;

import arez.annotations.ArezComponentLike;
import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class ManagedObservableAutoObserveArezComponentLikeReference
{
  @AutoObserve( validateTypeAtRuntime = true )
  @Observable
  abstract @Nullable MyComponent getMyComponent();

  abstract void setMyComponent( @Nullable MyComponent component );

  @NullMarked
  @ArezComponentLike
  public interface MyComponent
  {
  }
}
