package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;

@ArezComponent
public abstract class BadName2Model
{
  @Memoize( name = "public" )
  public int setField()
  {
    return 0;
  }
}
