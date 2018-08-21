package com.example.autorun;

import arez.Priority;
import arez.annotations.ArezComponent;
import arez.annotations.Autorun;

@ArezComponent
public abstract class HighPriorityAutorunModel
{
  @Autorun( priority = Priority.HIGH )
  protected void doStuff()
  {
  }
}
