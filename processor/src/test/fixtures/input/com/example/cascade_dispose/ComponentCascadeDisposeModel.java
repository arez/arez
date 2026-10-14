package com.example.cascade_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class ComponentCascadeDisposeModel
{
  @CascadeDispose
  @Nullable MyComponent _myElement;

  @NullMarked
  @ArezComponent( allowEmpty = true )
  abstract static class MyComponent
  {
  }
}
