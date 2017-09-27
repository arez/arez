package com.example.computed;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;

@ArezComponent
public class PrivateComputedModel
{
  @Computed
  private long getField()
  {
    return 0;
  }
}
