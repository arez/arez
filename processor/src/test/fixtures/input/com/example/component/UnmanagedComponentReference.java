package com.example.component;

import arez.annotations.ArezComponent;

@ArezComponent( allowEmpty = true )
abstract class UnmanagedComponentReference
{
  final MyComponent _myComponent = null;

  @ArezComponent( allowEmpty = true )
  public abstract static class MyComponent
  {
  }
}
