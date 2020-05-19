package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.Priority;

@ArezComponent
abstract class LowPriorityObserveModel
{
  @Observe( priority = Priority.LOW )
  void doStuff()
  {
  }
}
