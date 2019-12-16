package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Memoize;

@ArezComponent( defaultReadOutsideTransaction = Feature.DISABLE )
abstract class ReadOutsideTransactionFromDisabledDefaultMemoizeModel
{
  @Memoize
  public long getTime()
  {
    return 0;
  }
}
