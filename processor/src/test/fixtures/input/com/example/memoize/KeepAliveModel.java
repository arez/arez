package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class KeepAliveModel
{
  @Memoize( keepAlive = true )
  public long getTime()
  {
    return 0;
  }
}
