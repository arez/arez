package com.example.name_duplicates;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.OnActivate;

@ArezComponent
public class ActionAndOnActivateMethodModel
{
  @Action
  @OnActivate
  public long doStuff()
  {
    return 22;
  }
}
