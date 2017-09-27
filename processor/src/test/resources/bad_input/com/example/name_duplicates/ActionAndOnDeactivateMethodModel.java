package com.example.name_duplicates;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.OnDeactivate;

@ArezComponent
public class ActionAndOnDeactivateMethodModel
{
  @Action
  @OnDeactivate
  public long doStuff()
  {
    return 22;
  }
}
