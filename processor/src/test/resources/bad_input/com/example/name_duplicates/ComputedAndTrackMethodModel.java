package com.example.name_duplicates;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.Track;

@ArezComponent
public abstract class ComputedAndTrackMethodModel
{
  @Computed
  @Track
  public long getField()
  {
    return 22;
  }
}
