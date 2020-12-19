package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;

@ArezComponent( allowEmpty = true, dagger = Feature.ENABLE, sting = Feature.DISABLE )
public abstract class UnmanagedComponentReferenceDaggerInjected
{
  final MyComponent _myComponent;

  UnmanagedComponentReferenceDaggerInjected( int someOtherParameter,
                                             float velocity,
                                             final MyComponent myComponent )
  {
    _myComponent = myComponent;
  }

  @ArezComponent( allowEmpty = true )
  public abstract static class MyComponent
  {
  }
}
