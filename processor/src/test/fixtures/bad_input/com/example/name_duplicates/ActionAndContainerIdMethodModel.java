package com.example.name_duplicates;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class ActionAndContainerIdMethodModel
{
  @ComponentId
  @Action
  public long doStuff()
  {
    return 22;
  }
}
