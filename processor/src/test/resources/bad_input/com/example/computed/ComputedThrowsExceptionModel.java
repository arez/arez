package com.example.computed;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;

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
