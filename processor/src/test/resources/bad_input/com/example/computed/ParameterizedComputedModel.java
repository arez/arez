package com.example.computed;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;

@ArezComponent
public class ParameterizedComputedModel
{
  @Computed
  public long getField( final int param )
  {
    return 0;
  }
}
