package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;

@ArezComponent
abstract class OnStaleModel
{
  @Memoize
  long getTime()
  {
    return 0;
  }
}
