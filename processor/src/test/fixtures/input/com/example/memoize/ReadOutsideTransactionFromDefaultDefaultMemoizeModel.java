package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;

@ArezComponent
abstract class ReadOutsideTransactionFromDefaultDefaultMemoizeModel
{
  @Memoize
  public long getTime()
  {
    return 0;
  }
}
