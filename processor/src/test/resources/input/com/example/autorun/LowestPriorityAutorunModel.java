package com.example.autorun;

import arez.annotations.ArezComponent;
import arez.annotations.Autorun;
import arez.annotations.Priority;

@ArezComponent
public abstract class LowestPriorityAutorunModel
{
  @Autorun( priority = Priority.LOWEST )
  protected void doStuff()
  {
  }
}
