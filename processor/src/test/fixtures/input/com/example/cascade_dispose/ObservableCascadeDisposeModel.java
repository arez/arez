package com.example.cascade_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import arez.annotations.Observable;
import javax.annotation.Nonnull;

@ArezComponent
abstract class ObservableCascadeDisposeModel
{
  @CascadeDispose
  @Observable
  abstract MyComponent getElement();

  abstract void setElement( @Nonnull MyComponent element );

  @ArezComponent( allowEmpty = true )
  abstract static class MyComponent
  {
  }
}
