package com.example.autorun;

import arez.annotations.ArezComponent;
import arez.annotations.Autorun;
import arez.annotations.PostConstruct;

@ArezComponent
public abstract class ScheduleAfterConstructedModel
{
  @PostConstruct
  public void postConstruct()
  {
  }

  @Autorun
  public void doStuff()
  {
  }
}
