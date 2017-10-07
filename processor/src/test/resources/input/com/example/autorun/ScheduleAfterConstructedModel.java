package com.example.autorun;

import javax.annotation.PostConstruct;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Autorun;

@ArezComponent
public class ScheduleAfterConstructedModel
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
