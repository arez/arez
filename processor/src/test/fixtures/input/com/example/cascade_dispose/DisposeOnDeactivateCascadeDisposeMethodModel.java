package com.example.cascade_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;

@ArezComponent
abstract class DisposeOnDeactivateCascadeDisposeMethodModel
{
  @CascadeDispose
  MyComponent myElement()
  {
    return null;
  }

  @ArezComponent( allowEmpty = true, disposeOnDeactivate = true )
  abstract static class MyComponent
  {
  }
}
