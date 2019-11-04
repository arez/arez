package com.example.cascade_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import arez.annotations.Observable;
import javax.annotation.Nonnull;

@ArezComponent
abstract class Observable2CascadeDisposeModel
{
  @CascadeDispose
  abstract MyComponent getElement();

  @Observable
  abstract void setElement( @Nonnull MyComponent element );

  @ArezComponent( allowEmpty = true )
  static abstract class MyComponent
  {
  }
}
