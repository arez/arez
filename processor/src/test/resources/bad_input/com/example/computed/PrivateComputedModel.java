package com.example.computed;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;

@ArezComponent
public abstract class PrivateComputedModel
{
  @Computed
  private long getField()
  {
    return 0;
  }
}
