package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent( allowEmpty = true, sting = Feature.ENABLE )
abstract class UnmanagedComponentReferenceStingInjected
{
  final @Nullable MyComponent _myComponent;

  UnmanagedComponentReferenceStingInjected( int someOtherParameter,
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
