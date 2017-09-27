package com.example.name_duplicates;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.PreDispose;

@ArezComponent
public class ActionAndPreDisposeMethodModel
{
  @Action
  @PreDispose
  public long doStuff()
  {
    return 22;
  }
}
