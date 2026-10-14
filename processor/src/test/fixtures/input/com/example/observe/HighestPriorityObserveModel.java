package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.Priority;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class HighestPriorityObserveModel
{
  @Observe( priority = Priority.HIGHEST )
  void doStuff()
  {
  }
}
