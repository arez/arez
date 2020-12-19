package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;

@ArezComponent( allowEmpty = true, sting = Feature.ENABLE, dagger = Feature.DISABLE )
abstract class UnmanagedComponentReferenceStingInjected
{
  final MyComponent _myComponent;

  UnmanagedComponentReferenceStingInjected( int someOtherParameter,
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
