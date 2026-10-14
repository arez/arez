package com.example.name_duplicates;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class ActionAndMemoizeMethodModel
{
  @Memoize
  @Action
  public long doStuff()
  {
    return 22;
  }
}
