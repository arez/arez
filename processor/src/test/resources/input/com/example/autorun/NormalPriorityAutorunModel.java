package com.example.autorun;

import arez.annotations.Priority;
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
