package com.example.name_duplicates;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.OnDeactivate;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class ActionAndOnDeactivateMethodModel
{
  @Action
  @OnDeactivate
  public long doStuff()
  {
    return 22;
  }
}
