package com.example.cascade_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class BadType1Component
{
  // Should be declared type (a.k.a Class type)
  @CascadeDispose
  int _myField;
}
