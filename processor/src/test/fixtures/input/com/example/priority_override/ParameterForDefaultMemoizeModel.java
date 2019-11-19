package com.example.priority_override;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.PriorityOverride;

@ArezComponent
abstract class ParameterForDefaultMemoizeModel
{
  @Memoize
  public long getTime()
  {
    return 0;
  }

  @PriorityOverride
  final int timePriority()
  {
    return Observer.Flags.PRIORITY_LOW;
  }
}
