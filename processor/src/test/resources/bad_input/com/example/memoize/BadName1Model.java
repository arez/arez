package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;

@ArezComponent
public abstract class BadName1Model
{
  @Memoize( name = "-ace" )
  public int setField()
  {
    return 0;
  }
}
