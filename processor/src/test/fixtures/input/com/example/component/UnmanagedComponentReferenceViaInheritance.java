package com.example.component;

import arez.annotations.ArezComponent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
abstract class UnmanagedComponentReferenceViaInheritance
{
  @NullMarked
  @ArezComponent( allowEmpty = true )
  public abstract static class MyComponent
    extends BaseComponent
  {
  }

  @NullMarked
  public abstract static class BaseComponent
  {
    final @Nullable OtherComponent _component = null;
  }

  @NullMarked
  @ArezComponent( allowEmpty = true )
  abstract static class OtherComponent
  {
  }
}
