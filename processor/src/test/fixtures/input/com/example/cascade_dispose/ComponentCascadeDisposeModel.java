package com.example.cascade_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;

@ArezComponent
public abstract class ComponentCascadeDisposeModel
{
  @CascadeDispose
  protected MyComponent _myElement;

  @ArezComponent( allowEmpty = true )
  static abstract class MyComponent
  {
  }
}
