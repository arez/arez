package com.example.component;

import arez.annotations.ArezComponent;

abstract class UnmanagedComponentReferenceViaInheritance
{
  @ArezComponent( allowEmpty = true )
  public abstract static class MyComponent
    extends BaseComponent
  {
  }

  public abstract static class BaseComponent
  {
    final OtherComponent _component = null;
  }

  @ArezComponent( allowEmpty = true )
  abstract static class OtherComponent
  {
  }
}
