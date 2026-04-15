package com.example.component;

import arez.annotations.ArezComponentLike;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@SuppressWarnings( "Arez:UnmanagedComponentReference" )
@ArezComponent
abstract class UnmanagedObservableArezComponentLikeReferenceSuppressedOnClass
{
  @Observable
  abstract MyComponent getMyComponent();

  abstract void setMyComponent( MyComponent component );

  @ArezComponentLike
  public interface MyComponent
  {
  }
}
