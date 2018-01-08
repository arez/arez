package com.example.name_duplicates;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.OnDeactivate;

@ArezComponent
public class ActionAndOnDeactivateMethodModel
{
  @Action
  @OnDeactivate
  public long doStuff()
  {
    return 22;
  }
}
