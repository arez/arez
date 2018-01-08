package com.example.computed;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;

@ArezComponent
public class ParameterizedComputedModel
{
  @Computed
  public long getField( final int param )
  {
    return 0;
  }
}
