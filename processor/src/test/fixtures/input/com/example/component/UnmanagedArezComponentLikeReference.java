package com.example.component;

import arez.annotations.ArezComponentLike;
import arez.annotations.ArezComponent;

@ArezComponent( allowEmpty = true )
abstract class UnmanagedArezComponentLikeReference
{
  final MyComponent _myComponent = null;

  @ArezComponentLike
  public abstract static class MyComponent
  {
  }
}
