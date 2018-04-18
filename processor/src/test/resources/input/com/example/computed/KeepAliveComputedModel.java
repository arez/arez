package com.example.computed;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;

@ArezComponent
public abstract class KeepAliveComputedModel
{
  @Computed( keepAlive = true )
  public long getTime()
  {
    return 0;
  }
}
