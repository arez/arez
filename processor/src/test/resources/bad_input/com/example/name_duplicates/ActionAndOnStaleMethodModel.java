package com.example.name_duplicates;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.OnStale;

@ArezComponent
public class ActionAndOnStaleMethodModel
{
  @Action
  @OnStale
  public long doStuff()
  {
    return 22;
  }
}
