package com.example.component;

import arez.annotations.ActAsComponent;
import arez.annotations.ArezComponent;

@ArezComponent( allowEmpty = true )
abstract class UnmanagedActAsComponentReference
{
  final MyComponent _myComponent = null;

  @ActAsComponent
  public abstract static class MyComponent
  {
  }
}
