package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;

@ArezComponent
public abstract class DuplicateModel
{
  @Memoize( name = "ace" )
  public int getX()
  {
    return 0;
  }

  @Memoize( name = "ace" )
  public int getX2()
  {
    return 0;
  }
}
