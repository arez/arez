package com.example.priority_override;

import arez.Observer;
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
    return Observer.Flags.PRIORITY_LOW;
  }
}
