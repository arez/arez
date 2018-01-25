package com.example.memoized;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;

@ArezComponent
public abstract class AbstractMemoizeModel
{
  @Memoize
  abstract int getField( int key );
}
