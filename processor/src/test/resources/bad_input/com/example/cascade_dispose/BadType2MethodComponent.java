package com.example.cascade_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;

@ArezComponent
public abstract class BadType2MethodComponent
{
  // Class does not implement Disposable
  @CascadeDispose
  final String myField()
  {
    return null;
  }
}
