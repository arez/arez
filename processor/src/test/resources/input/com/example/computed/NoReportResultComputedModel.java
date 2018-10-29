package com.example.computed;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;

@ArezComponent
public abstract class NoReportResultComputedModel
{
  @Computed( reportResult = false )
  public long getTime()
  {
    return 0;
  }
}
