package com.example.computed;

import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;

@Container
public class BasicComputedModel
{
  @Computed
  public long getTime()
  {
    return 0;
  }
}
