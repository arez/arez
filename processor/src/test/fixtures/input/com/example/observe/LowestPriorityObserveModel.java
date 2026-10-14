package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.Priority;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class LowestPriorityObserveModel
{
  @Observe( priority = Priority.LOWEST )
  void doStuff()
  {
  }
}
