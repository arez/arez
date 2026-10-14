package com.example.component;

import arez.annotations.ArezComponentLike;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class UnmanagedObservableArezComponentLikeReferenceSuppressed
{
  @SuppressWarnings( "Arez:UnmanagedComponentReference" )
  @Observable
  abstract @Nullable MyComponent getMyComponent();

  abstract void setMyComponent( @Nullable MyComponent component );

  @NullMarked
  @ArezComponentLike
  public interface MyComponent
  {
  }
}
