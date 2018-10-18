package com.example.memoized;

import arez.annotations.ArezComponent;
import arez.annotations.DepType;
import arez.annotations.Memoize;

@ArezComponent
public abstract class BadDepTypeMemoizeModel
{
  @Memoize( depType = DepType.AREZ_OR_EXTERNAL )
  int getField( int key )
  {
    return 0;
  }
}
