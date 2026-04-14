package com.example.component;

import arez.annotations.ActAsComponent;
import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;
import arez.annotations.Observable;

@ArezComponent
abstract class ManagedObservableAutoObserveActAsComponentReference
{
  @AutoObserve( validateTypeAtRuntime = true )
  @Observable
  abstract MyComponent getMyComponent();

  abstract void setMyComponent( MyComponent component );

  @ActAsComponent
  public interface MyComponent
  {
  }
}
