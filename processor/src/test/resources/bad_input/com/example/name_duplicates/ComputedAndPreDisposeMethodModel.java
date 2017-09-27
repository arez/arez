package com.example.name_duplicates;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.PreDispose;

@ArezComponent
public class ComputedAndPreDisposeMethodModel
{
  @Computed
  @PreDispose
  public long getField()
  {
    return 22;
  }
}
