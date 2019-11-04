package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;

@ArezComponent
public abstract class KeepAliveWithParametersModel
{
  @Memoize( keepAlive = true )
  public long getField( int i )
  {
    return 0;
  }
}
