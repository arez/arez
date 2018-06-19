package com.example.autorun;

import arez.annotations.ArezComponent;
import arez.annotations.Autorun;
import arez.annotations.Priority;

@ArezComponent
public abstract class NormalPriorityAutorunModel
{
  @Autorun( priority = Priority.NORMAL )
  protected void doStuff()
  {
  }
}
