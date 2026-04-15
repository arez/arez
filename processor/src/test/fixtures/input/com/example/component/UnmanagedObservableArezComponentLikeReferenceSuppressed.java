package com.example.component;

import arez.annotations.ArezComponentLike;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
abstract class UnmanagedObservableArezComponentLikeReferenceSuppressed
{
  @SuppressWarnings( "Arez:UnmanagedComponentReference" )
  @Observable
  abstract MyComponent getMyComponent();

  abstract void setMyComponent( MyComponent component );

  @ArezComponentLike
  public interface MyComponent
  {
  }
}
