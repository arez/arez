package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;

@ArezComponent
public abstract class PrivateModel
{
  @Memoize
  private long getField()
  {
    return 0;
  }
}
