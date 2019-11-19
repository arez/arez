package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;

@ArezComponent( allowEmpty = true )
abstract class UnmanagedComponentReferenceToNonDisposeNotifier
{
  final MyComponent _myComponent = null;

  @ArezComponent( allowEmpty = true, disposeNotifier = Feature.DISABLE )
  public static abstract class MyComponent
  {
  }
}
