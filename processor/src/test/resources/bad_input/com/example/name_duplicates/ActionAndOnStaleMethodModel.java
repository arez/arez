package com.example.name_duplicates;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.OnStale;

@ArezComponent
public abstract class ActionAndOnStaleMethodModel
{
  @Action
  @OnStale
  public long doStuff()
  {
    return 22;
  }
}
