package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;

@ArezComponent
public abstract class StaticModel
{
  @Memoize
  public static long getField()
  {
    return 0;
  }
}
