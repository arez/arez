package com.example.computed;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;

@ArezComponent
public class ComputedThrowsExceptionModel
{
  @Computed
  public long getField()
    throws Exception
  {
    return 0;
  }
}
