package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.component.DisposeNotifier;

@ArezComponent
public abstract class UnmanagedObservableDisposeNotifierReference
{
  @Observable
  abstract MyComponent getMyComponent();

  abstract void setMyComponent( MyComponent component );

  public static abstract class MyComponent
    implements DisposeNotifier
  {
  }
}
