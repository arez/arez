package com.example.memoized;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;

@ArezComponent
public abstract class NoParamMemoizeModel
{
  @Memoize
  int getField()
  {
    return 0;
  }
}
