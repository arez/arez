package com.example.priority_override;

import arez.Flags;
import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.PriorityOverride;

@ArezComponent
public abstract class BadReturnTypeModel
{
  @Observe
  protected void doStuff()
  {
  }

  @PriorityOverride
  final long doStuffPriority()
  {
    return Flags.PRIORITY_LOW;
  }
}
