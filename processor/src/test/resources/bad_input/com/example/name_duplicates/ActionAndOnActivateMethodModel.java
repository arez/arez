package com.example.name_duplicates;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.OnActivate;

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
