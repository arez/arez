package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class UnmanagedObservableComponentReference
{
  @Observable
  abstract @Nullable MyComponent getMyComponent();

  abstract void setMyComponent( @Nullable MyComponent component );

  @NullMarked
  @ArezComponent( allowEmpty = true )
  public abstract static class MyComponent
  {
  }
}
