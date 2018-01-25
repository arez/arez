package com.example.memoized;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;

@ArezComponent
public abstract class FinalMemoizeModel
{
  @Memoize
  final int getField( int key )
  {
    return 0;
  }
}
