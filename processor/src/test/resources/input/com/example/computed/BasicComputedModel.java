package com.example.computed;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;

@ArezComponent
public abstract class BasicComputedModel
{
  @Computed
  public long getTime()
  {
    return 0;
  }
}
