package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.Priority;

@ArezComponent
abstract class HighestPriorityObserveModel
{
  @Observe( priority = Priority.HIGHEST )
  void doStuff()
  {
  }
}
