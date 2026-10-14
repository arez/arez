package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class UnmanagedObservableComponentReferenceToNonVerify
{
  @Observable
  abstract @Nullable MyComponent getMyComponent();

  abstract void setMyComponent( @Nullable MyComponent component );

  @NullMarked
  @ArezComponent( allowEmpty = true, verifyReferencesToComponent = Feature.DISABLE )
  public abstract static class MyComponent
  {
  }
}
