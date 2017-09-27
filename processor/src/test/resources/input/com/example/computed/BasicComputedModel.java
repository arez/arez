package com.example.computed;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;

@ArezComponent
public class BasicComputedModel
{
  @Computed
  public long getTime()
  {
    return 0;
  }
}
