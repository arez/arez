package com.example.priority_override;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.PriorityOverride;

@ArezComponent
public abstract class BadNameModel3
{
  @Observe
  protected void doStuff()
  {
  }

  @PriorityOverride
  final int prioritizeDoStuff()
  {
    return Observer.Flags.PRIORITY_LOW;
  }
}
