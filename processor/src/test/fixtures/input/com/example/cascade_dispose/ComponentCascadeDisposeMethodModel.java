package com.example.cascade_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;

@ArezComponent
abstract class ComponentCascadeDisposeMethodModel
{
  @CascadeDispose
  MyComponent myElement()
  {
    return null;
  }

  @ArezComponent( allowEmpty = true )
  abstract static class MyComponent
  {
  }
}
