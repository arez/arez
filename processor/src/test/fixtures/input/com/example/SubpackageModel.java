package com.example;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Observable;

@ArezComponent
public abstract class SubpackageModel
{
  @Observable
  public long getTime()
  {
    return 0;
  }

  @Observable
  public void setTime( final long time )
  {
  }

  @Action
  public void doStuff( final long time )
  {
  }

  @Memoize
  public int someValue()
  {
    return 0;
  }
}
