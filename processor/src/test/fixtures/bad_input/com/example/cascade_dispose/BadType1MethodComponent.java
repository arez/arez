package com.example.cascade_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;

@ArezComponent
public abstract class BadType1MethodComponent
{
  // Should be declared type (a.k.a Class type)
  @CascadeDispose
  final int myField()
  {
    return 0;
  }
}
