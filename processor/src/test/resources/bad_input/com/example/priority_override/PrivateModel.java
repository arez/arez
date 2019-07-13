package com.example.priority_override;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.PriorityOverride;

@ArezComponent
public abstract class PrivateModel
{
  @Observe
  protected void doStuff()
  {
  }

  @PriorityOverride
  private int doStuffPriority()
  {
    return Observer.Flags.PRIORITY_LOW;
  }
}
