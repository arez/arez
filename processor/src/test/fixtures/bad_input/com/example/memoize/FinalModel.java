package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;

@ArezComponent
public abstract class FinalModel
{
  @Memoize
  public final long getField()
  {
    return 0;
  }
}
