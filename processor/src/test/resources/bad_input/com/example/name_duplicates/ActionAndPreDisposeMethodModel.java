package com.example.name_duplicates;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.PreDispose;

@ArezComponent
public class ActionAndPreDisposeMethodModel
{
  @Action
  @PreDispose
  public long doStuff()
  {
    return 22;
  }
}
