package com.example.computed;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;

@ArezComponent
public class StaticComputedModel
{
  @Computed
  public static long getField()
  {
    return 0;
  }
}
