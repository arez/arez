package com.example.priority_override;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.PriorityOverride;

@ArezComponent
public abstract class StaticModel
{
  @Observe
  protected void doStuff()
  {
  }

  @PriorityOverride
  static int doStuffPriority()
  {
    return Observer.Flags.PRIORITY_LOW;
  }
}
