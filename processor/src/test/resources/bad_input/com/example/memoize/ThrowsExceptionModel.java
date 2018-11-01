package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;

@ArezComponent
public abstract class ThrowsExceptionModel
{
  @Memoize
  public long getField()
    throws Exception
  {
    return 0;
  }
}
