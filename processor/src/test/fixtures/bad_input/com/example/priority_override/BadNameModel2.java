package com.example.priority_override;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.PriorityOverride;

@ArezComponent
public abstract class BadNameModel2
{
  @Observe
  protected void doStuff()
  {
  }

  @PriorityOverride( name = "int" )
  final int doStuffPriority()
  {
    return Observer.Flags.PRIORITY_LOW;
  }
}
