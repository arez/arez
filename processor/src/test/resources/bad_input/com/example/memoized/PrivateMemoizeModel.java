package com.example.memoized;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;

@ArezComponent
public abstract class PrivateMemoizeModel
{
  @Memoize
  private int getField( int key )
  {
    return 0;
  }
}
