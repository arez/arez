package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Memoize;

@ArezComponent
abstract class ReadOutsideTransactionEnabledMemoizeModel
{
  @Memoize( readOutsideTransaction = Feature.ENABLE )
  public long getTime()
  {
    return 0;
  }
}
