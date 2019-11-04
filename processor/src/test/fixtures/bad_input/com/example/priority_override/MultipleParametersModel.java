package com.example.priority_override;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.PriorityOverride;

@ArezComponent
public abstract class MultipleParametersModel
{
  @Observe
  protected void doStuff()
  {
  }

  @PriorityOverride
  final int doStuffPriority( final int defaultPriority, final int other )
  {
    return Observer.Flags.PRIORITY_LOW;
  }
}
