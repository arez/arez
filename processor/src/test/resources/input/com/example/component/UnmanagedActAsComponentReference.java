package com.example.component;

import arez.annotations.ActAsComponent;
import arez.annotations.ArezComponent;

@ArezComponent( allowEmpty = true )
public abstract class UnmanagedActAsComponentReference
{
  final MyComponent _myComponent = null;

  @ActAsComponent
  public static abstract class MyComponent
  {
  }
}
