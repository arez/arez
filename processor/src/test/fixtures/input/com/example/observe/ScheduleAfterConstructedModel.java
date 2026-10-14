package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.PostConstruct;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class ScheduleAfterConstructedModel
{
  @PostConstruct
  void postConstruct()
  {
  }

  @Observe
  void doStuff()
  {
  }
}
