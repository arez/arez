package com.example.priority_override;

import arez.Flags;
import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.PriorityOverride;

@ArezComponent
public abstract class CustomNameModel
{
  @Observe
  protected void doStuff()
  {
  }

  @PriorityOverride( name = "doStuff" )
  final int prioritizeDoStuff()
  {
    return Flags.PRIORITY_LOW;
  }
}
