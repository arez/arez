package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;

@ArezComponent( allowEmpty = true )
abstract class UnmanagedComponentReferenceToNonVerify
{
  final MyComponent _myComponent = null;

  @ArezComponent( allowEmpty = true, verifyReferencesToComponent = Feature.DISABLE )
  public static abstract class MyComponent
  {
  }
}
