package com.example.priority_override;

import arez.Flags;
import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.PriorityOverride;

@ArezComponent
public abstract class ParameterForDefaultObserveModel
{
  @Observe
  protected void doStuff()
  {
  }

  @PriorityOverride
  final int doStuffPriority( final int defaultPriority )
  {
    return Flags.PRIORITY_LOW;
  }
}
