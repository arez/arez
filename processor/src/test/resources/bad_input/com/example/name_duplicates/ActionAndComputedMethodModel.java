package com.example.name_duplicates;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;

@ArezComponent
public class ActionAndComputedMethodModel
{
  @Computed
  @Action
  public long doStuff()
  {
    return 22;
  }
}
