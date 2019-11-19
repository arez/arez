package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.Priority;

@ArezComponent
abstract class NormalPriorityObserveModel
{
  @Observe( priority = Priority.NORMAL )
  protected void doStuff()
  {
  }
}
