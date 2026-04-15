package com.example.component;

import arez.annotations.ArezComponentLike;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
abstract class UnmanagedObservableArezComponentLikeReferenceSuppressedOnSetter
{
  @Observable
  abstract MyComponent getMyComponent();

  @SuppressWarnings( "Arez:UnmanagedComponentReference" )
  abstract void setMyComponent( MyComponent component );

  @ArezComponentLike
  public interface MyComponent
  {
  }
}
