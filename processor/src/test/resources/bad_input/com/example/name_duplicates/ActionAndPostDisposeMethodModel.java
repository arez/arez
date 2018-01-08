package com.example.name_duplicates;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.PostDispose;

@ArezComponent
public class ActionAndPostDisposeMethodModel
{
  @Action
  @PostDispose
  public long doStuff()
  {
    return 22;
  }
}
