package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Memoize;

@ArezComponent( defaultReadOutsideTransaction = Feature.ENABLE )
abstract class ReadOutsideTransactionFromEnabledDefaultMemoizeModel
{
  @Memoize
  public long getTime()
  {
    return 0;
  }
}
