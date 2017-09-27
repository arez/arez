package com.example.name_duplicates;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.PostDispose;

@ArezComponent
public class ActionAndPostDisposeMethodModel
{
  @Action
  @PostDispose
  public long doStuff()
  {
    return 22;
  }
}
