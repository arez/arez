package com.example.component;

import arez.annotations.ArezComponentLike;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@SuppressWarnings( "Arez:UnmanagedComponentReference" )
@ArezComponent
abstract class UnmanagedObservableArezComponentLikeReferenceSuppressedOnClass
{
  @Observable
  abstract @Nullable MyComponent getMyComponent();

  abstract void setMyComponent( @Nullable MyComponent component );

  @NullMarked
  @ArezComponentLike
  public interface MyComponent
  {
  }
}
