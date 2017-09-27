package com.example.name_duplicates;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.OnStale;

@ArezComponent
public class ComputedAndOnStaleMethodModel
{
  @Computed
  @OnStale
  public long getField()
  {
    return 22;
  }
}
