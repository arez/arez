package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;

@ArezComponent
abstract class ReadOutsideTransactionModel
{
  @Memoize( readOutsideTransaction = true )
  public long getTime()
  {
    return 0;
  }
}
