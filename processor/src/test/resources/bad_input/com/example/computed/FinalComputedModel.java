package com.example.computed;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;

@ArezComponent
public class FinalComputedModel
{
  @Computed
  public final long getField()
  {
    return 0;
  }
}
