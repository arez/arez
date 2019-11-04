package com.example.priority_override;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.PriorityOverride;
import java.io.IOException;

@ArezComponent
public abstract class ThrowsModel
{
  @Observe
  protected void doStuff()
  {
  }

  @PriorityOverride
  int doStuffPriority()
    throws IOException
  {
    return Observer.Flags.PRIORITY_LOW;
  }
}
