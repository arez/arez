package com.example.cascade_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import java.util.List;

@ArezComponent
public abstract class BadType3MethodComponent
{
  // Class does not implement Disposable
  @CascadeDispose
  final List<?> myField()
  {
    return null;
  }
}
