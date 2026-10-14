package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.component.DisposeNotifier;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class UnmanagedObservableDisposeNotifierReference
{
  @Observable
  abstract @Nullable MyComponent getMyComponent();

  abstract void setMyComponent( @Nullable MyComponent component );

  @NullMarked
  public abstract static class MyComponent
    implements DisposeNotifier
  {
  }
}
