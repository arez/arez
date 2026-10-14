package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class UnmanagedComponentReferenceToNonDisposeNotifier
{
  final @Nullable MyComponent _myComponent = null;

  @NullMarked
  @ArezComponent( allowEmpty = true, disposeNotifier = Feature.DISABLE )
  public abstract static class MyComponent
  {
  }
}
