package com.example.priority_override;

import arez.Flags;
import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.PriorityOverride;

@ArezComponent
public abstract class BadNameModel
{
  @Observe
  protected void doStuff()
  {
  }

  @PriorityOverride( name = "-ace" )
  final int doStuffPriority()
  {
    return Flags.PRIORITY_LOW;
  }
}
