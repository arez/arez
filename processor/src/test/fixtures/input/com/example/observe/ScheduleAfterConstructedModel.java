package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.PostConstruct;

@ArezComponent
abstract class ScheduleAfterConstructedModel
{
  @PostConstruct
  public void postConstruct()
  {
  }

  @Observe
  protected void doStuff()
  {
  }
}
