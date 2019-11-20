package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.Priority;

@ArezComponent( defaultPriority = Priority.DEFAULT )
abstract class DefaultDefaultPriorityUnspecifiedLocalPriorityObserveModel
{
  @Observe
  void doStuff()
  {
  }
}
