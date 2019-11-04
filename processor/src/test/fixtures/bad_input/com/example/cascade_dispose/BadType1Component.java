package com.example.cascade_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;

@ArezComponent
public abstract class BadType1Component
{
  // Should be declared type (a.k.a Class type)
  @CascadeDispose
  int _myField;
}
