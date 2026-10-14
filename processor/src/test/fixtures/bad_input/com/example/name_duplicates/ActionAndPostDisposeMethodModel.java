package com.example.name_duplicates;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.PostDispose;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class ActionAndPostDisposeMethodModel
{
  @Action
  @PostDispose
  public long doStuff()
  {
    return 22;
  }
}
