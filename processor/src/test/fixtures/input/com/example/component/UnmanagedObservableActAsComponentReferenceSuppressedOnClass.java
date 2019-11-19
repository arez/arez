package com.example.component;

import arez.annotations.ActAsComponent;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@SuppressWarnings( "Arez:UnmanagedComponentReference" )
@ArezComponent
abstract class UnmanagedObservableActAsComponentReferenceSuppressedOnClass
{
  @Observable
  abstract MyComponent getMyComponent();

  abstract void setMyComponent( MyComponent component );

  @ActAsComponent
  public interface MyComponent
  {
  }
}
