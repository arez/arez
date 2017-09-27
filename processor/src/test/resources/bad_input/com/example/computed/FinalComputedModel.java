package com.example.computed;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;

@ArezComponent
public class FinalComputedModel
{
  @Computed
  public final long getField()
  {
    return 0;
  }
}
