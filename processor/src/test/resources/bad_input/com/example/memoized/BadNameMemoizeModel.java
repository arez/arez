package com.example.memoized;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;

@ArezComponent
public abstract class BadNameMemoizeModel
{
  @Memoize(name = "-ace")
  int getField( int key )
  {
    return 0;
  }
}
