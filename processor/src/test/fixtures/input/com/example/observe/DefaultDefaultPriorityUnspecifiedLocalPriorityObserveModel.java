package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.Priority;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( defaultPriority = Priority.DEFAULT )
abstract class DefaultDefaultPriorityUnspecifiedLocalPriorityObserveModel
{
  @Observe
  void doStuff()
  {
  }
}
