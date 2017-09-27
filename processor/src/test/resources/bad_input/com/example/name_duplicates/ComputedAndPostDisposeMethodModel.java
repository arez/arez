package com.example.name_duplicates;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.PostDispose;

@ArezComponent
public class ComputedAndPostDisposeMethodModel
{
  @Computed
  @PostDispose
  public long getField()
  {
    return 22;
  }
}
