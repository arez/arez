package com.example.memoized;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;

@ArezComponent
public abstract class BadName2MemoizeModel
{
  @Memoize( name = "protected" )
  int getField( int key )
  {
    return 0;
  }
}
