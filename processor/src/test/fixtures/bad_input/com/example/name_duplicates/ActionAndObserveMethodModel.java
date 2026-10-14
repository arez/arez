package com.example.name_duplicates;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class ActionAndObserveMethodModel
{
  @Observe
  @Action
  public void doStuff()
  {
  }
}
