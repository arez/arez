package com.example.name_duplicates;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Observe;

@ArezComponent
public abstract class ActionAndObservedMethodModel
{
  @Observe
  @Action
  public void doStuff()
  {
  }
}
