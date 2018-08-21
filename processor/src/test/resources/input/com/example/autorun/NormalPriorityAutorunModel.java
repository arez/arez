package com.example.autorun;

import arez.Priority;
import arez.annotations.ArezComponent;
import arez.annotations.Autorun;

@ArezComponent
public abstract class NormalPriorityAutorunModel
{
  @Autorun( priority = Priority.NORMAL )
  protected void doStuff()
  {
  }
}
