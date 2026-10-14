package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.Priority;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( defaultPriority = Priority.HIGH )
abstract class DefaultPrioritySpecifiedLocalPriorityObserveModel
{
  @Observe( priority = Priority.NORMAL )
  void doStuff()
  {
  }
}
