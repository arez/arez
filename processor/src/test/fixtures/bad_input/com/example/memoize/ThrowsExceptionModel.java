package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;

@ArezComponent
public abstract class ThrowsExceptionModel
{
  @SuppressWarnings( { "RedundantThrows", "RedundantSuppression" } )
  @Memoize
  public long getField()
    throws Exception
  {
    return 0;
  }
}
