package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.component.DisposeNotifier;

@ArezComponent
abstract class UnmanagedObservableDisposeNotifierReference
{
  @Observable
  abstract MyComponent getMyComponent();

  abstract void setMyComponent( MyComponent component );

  public abstract static class MyComponent
    implements DisposeNotifier
  {
  }
}
