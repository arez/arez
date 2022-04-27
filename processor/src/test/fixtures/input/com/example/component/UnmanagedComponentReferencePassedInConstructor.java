package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;

@ArezComponent( allowEmpty = true, sting = Feature.DISABLE )
abstract class UnmanagedComponentReferencePassedInConstructor
{
  final MyComponent _myComponent;

  UnmanagedComponentReferencePassedInConstructor( int someOtherParameter,
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
