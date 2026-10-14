package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent( allowEmpty = true, sting = Feature.DISABLE )
abstract class UnmanagedComponentReferencePassedInConstructor
{
  final @Nullable MyComponent _myComponent;

  UnmanagedComponentReferencePassedInConstructor( int someOtherParameter,
                                                  float velocity,
                                                  final @Nullable MyComponent myComponent )
  {
    _myComponent = myComponent;
  }

  @NullMarked
  @ArezComponent( allowEmpty = true )
  public abstract static class MyComponent
  {
  }
}
