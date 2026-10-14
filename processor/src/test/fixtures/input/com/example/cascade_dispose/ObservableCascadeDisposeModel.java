package com.example.cascade_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import arez.annotations.Observable;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class ObservableCascadeDisposeModel
{
  @CascadeDispose
  @Observable
  abstract @Nullable MyComponent getElement();

  abstract void setElement( @NonNull MyComponent element );

  @NullMarked
  @ArezComponent( allowEmpty = true )
  abstract static class MyComponent
  {
  }
}
