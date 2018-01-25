package com.example.computed;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;

@ArezComponent
public abstract class BadComputedNameModel
{
  @Computed( name = "-ace" )
  public int setField()
  {
    return 0;
  }
}
