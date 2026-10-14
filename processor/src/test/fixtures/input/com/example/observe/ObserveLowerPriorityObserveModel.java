package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class ObserveLowerPriorityObserveModel
{
  @Observe( observeLowerPriorityDependencies = true )
  void doStuff()
  {
  }
}
