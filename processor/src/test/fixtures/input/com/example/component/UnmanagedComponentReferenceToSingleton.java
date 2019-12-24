package com.example.component;

import arez.annotations.ArezComponent;
import javax.inject.Singleton;

@ArezComponent( allowEmpty = true )
abstract class UnmanagedComponentReferenceToSingleton
{
  final MyComponent _myComponent = null;

  @Singleton
  @ArezComponent( allowEmpty = true )
  public abstract static class MyComponent
  {
  }
}
