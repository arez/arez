package com.example.component;

import arez.annotations.ArezComponent;

@ArezComponent( allowEmpty = true )
public abstract class UnmanagedComponentReference
{
  final MyComponent _myComponent = null;

  @ArezComponent( allowEmpty = true )
  public static abstract class MyComponent
  {
  }
}
