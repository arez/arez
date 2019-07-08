package com.example.priority_override;

import arez.Flags;
import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.PriorityOverride;

@ArezComponent
public abstract class BadParameterModel
{
  @Observe
  protected void doStuff()
  {
  }

  @PriorityOverride
  int doStuffPriority( long badParam )
  {
    return Flags.PRIORITY_LOW;
  }
}
