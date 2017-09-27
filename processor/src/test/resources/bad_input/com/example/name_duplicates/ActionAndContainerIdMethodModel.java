package com.example.name_duplicates;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ComponentId;

@ArezComponent
public class ActionAndContainerIdMethodModel
{
  @ComponentId
  @Action
  public long doStuff()
  {
    return 22;
  }
}
