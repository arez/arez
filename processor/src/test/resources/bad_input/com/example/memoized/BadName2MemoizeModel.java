package com.example.memoized;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;

@ArezComponent
public class BadName2MemoizeModel
{
  @Memoize( name = "ace-" )
  int getField( int key )
  {
    return 0;
  }
}
