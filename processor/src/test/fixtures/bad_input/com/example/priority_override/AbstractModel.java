package com.example.priority_override;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.PriorityOverride;

@ArezComponent
public abstract class AbstractModel
{
  @Observe
  protected void doStuff()
  {
  }

  @PriorityOverride
  abstract int doStuffPriority();
}
