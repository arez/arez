package com.example.memoized;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;

@ArezComponent
public abstract class DuplicateMemoizeModel
{
  @Memoize
  int method1( int key )
  {
    return 0;
  }

  @Memoize( name = "method1" )
  int method2( int key )
  {
    return 0;
  }
}
