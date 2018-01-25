package com.example.memoized;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;

@ArezComponent
public abstract class VoidMemoizeModel
{
  @Memoize
  public void getField( int key )
  {
  }
}
