package com.example.component;

import arez.annotations.ActAsComponent;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
abstract class UnmanagedObservableActAsComponentReferenceSuppressedOnSetter
{
  @Observable
  abstract MyComponent getMyComponent();

  @SuppressWarnings( "Arez:UnmanagedComponentReference" )
  abstract void setMyComponent( MyComponent component );

  @ActAsComponent
  public interface MyComponent
  {
  }
}
