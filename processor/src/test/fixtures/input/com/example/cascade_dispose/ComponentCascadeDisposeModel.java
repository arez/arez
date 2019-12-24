package com.example.cascade_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;

@ArezComponent
abstract class ComponentCascadeDisposeModel
{
  @CascadeDispose
  protected MyComponent _myElement;

  @ArezComponent( allowEmpty = true )
  abstract static class MyComponent
  {
  }
}
