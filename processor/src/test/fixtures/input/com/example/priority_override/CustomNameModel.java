package com.example.priority_override;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.PriorityOverride;

@ArezComponent
abstract class CustomNameModel
{
  @Observe
  protected void doStuff()
  {
  }

  @PriorityOverride( name = "doStuff" )
  final int prioritizeDoStuff()
  {
    return Observer.Flags.PRIORITY_LOW;
  }
}
