package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Observed;
import arez.annotations.PostConstruct;

@ArezComponent
public abstract class ScheduleAfterConstructedModel
{
  @PostConstruct
  public void postConstruct()
  {
  }

  @Observed
  protected void doStuff()
  {
  }
}
