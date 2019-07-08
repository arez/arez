package com.example.priority_override;

import arez.Flags;
import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.PriorityOverride;

@ArezComponent
public abstract class ParameterForDefaultMemoizeModel
{
  @Memoize
  public long getTime()
  {
    return 0;
  }

  @PriorityOverride
  final int timePriority()
  {
    return Flags.PRIORITY_LOW;
  }
}
