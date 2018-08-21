package com.example.autorun;

import arez.Priority;
import arez.annotations.ArezComponent;
import arez.annotations.Autorun;

@ArezComponent
public abstract class LowestPriorityAutorunModel
{
  @Autorun( priority = Priority.LOWEST )
  protected void doStuff()
  {
  }
}
