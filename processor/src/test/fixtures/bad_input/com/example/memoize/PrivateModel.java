package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class PrivateModel
{
  @Memoize
  private long getField()
  {
    return 0;
  }
}
