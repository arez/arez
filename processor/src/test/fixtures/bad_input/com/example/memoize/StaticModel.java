package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class StaticModel
{
  @Memoize
  public static long getField()
  {
    return 0;
  }
}
