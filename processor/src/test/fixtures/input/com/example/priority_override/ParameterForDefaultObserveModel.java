package com.example.priority_override;

import arez.Observer;
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
    return Observer.Flags.PRIORITY_LOW;
  }
}
