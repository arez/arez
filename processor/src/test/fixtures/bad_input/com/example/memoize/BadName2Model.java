package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class BadName2Model
{
  @Memoize( name = "public" )
  public int setField()
  {
    return 0;
  }
}
