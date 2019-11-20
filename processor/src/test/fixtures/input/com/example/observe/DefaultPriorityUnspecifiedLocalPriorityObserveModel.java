package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.Priority;

@ArezComponent( defaultPriority = Priority.HIGH )
abstract class DefaultPriorityUnspecifiedLocalPriorityObserveModel
{
  @Observe
  void doStuff()
  {
  }
}
