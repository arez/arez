package com.example.cascade_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;

@ArezComponent
public abstract class ComponentCascadeDisposeMethodModel
{
  @CascadeDispose
  protected final MyComponent myElement()
  {
    return null;
  }

  @ArezComponent( allowEmpty = true )
  static abstract class MyComponent
  {
  }
}
