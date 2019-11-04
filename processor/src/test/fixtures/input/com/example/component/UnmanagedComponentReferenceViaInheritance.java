package com.example.component;

import arez.annotations.ArezComponent;

public abstract class UnmanagedComponentReferenceViaInheritance
{
  @ArezComponent( allowEmpty = true )
  public static abstract class MyComponent
    extends BaseComponent
  {
  }

  public static abstract class BaseComponent
  {
    final OtherComponent _component = null;
  }

  @ArezComponent( allowEmpty = true )
  static abstract class OtherComponent
  {
  }
}
