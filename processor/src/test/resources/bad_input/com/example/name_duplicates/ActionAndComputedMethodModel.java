package com.example.name_duplicates;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Computed;

@ArezComponent
public abstract class ActionAndComputedMethodModel
{
  @Computed
  @Action
  public long doStuff()
  {
    return 22;
  }
}
