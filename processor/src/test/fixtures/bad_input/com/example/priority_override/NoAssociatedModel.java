package com.example.priority_override;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.PriorityOverride;

@ArezComponent
public abstract class NoAssociatedModel
{
  @PriorityOverride
  final int doStuffPriority()
  {
    return Observer.Flags.PRIORITY_LOW;
  }
}
