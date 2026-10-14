package com.example.cascade_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class ComponentCascadeDisposeMethodModel
{
  @CascadeDispose
  @Nullable MyComponent myElement()
  {
    return null;
  }

  @NullMarked
  @ArezComponent( allowEmpty = true )
  abstract static class MyComponent
  {
  }
}
