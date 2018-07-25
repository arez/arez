package com.example.autorun;

import arez.annotations.ArezComponent;
import arez.annotations.Autorun;
import arez.annotations.Priority;

@ArezComponent
public abstract class HighestPriorityAutorunModel
{
  @Autorun( priority = Priority.HIGHEST )
  protected void doStuff()
  {
  }
}
