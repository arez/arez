package com.example.computed;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;

@ArezComponent
public class StaticComputedModel
{
  @Computed
  public static long getField()
  {
    return 0;
  }
}
