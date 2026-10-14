package com.example.component;

import arez.annotations.ArezComponent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class UnmanagedComponentReference
{
  final @Nullable MyComponent _myComponent = null;

  @NullMarked
  @ArezComponent( allowEmpty = true )
  public abstract static class MyComponent
  {
  }
}
