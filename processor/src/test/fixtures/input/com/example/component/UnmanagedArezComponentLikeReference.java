package com.example.component;

import arez.annotations.ArezComponentLike;
import arez.annotations.ArezComponent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class UnmanagedArezComponentLikeReference
{
  final @Nullable MyComponent _myComponent = null;

  @NullMarked
  @ArezComponentLike
  public abstract static class MyComponent
  {
  }
}
