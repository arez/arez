package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.Priority;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class NormalPriorityObserveModel
{
  @Observe( priority = Priority.NORMAL )
  void doStuff()
  {
  }
}
