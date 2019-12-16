package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Memoize;

@ArezComponent
abstract class ReadOutsideTransactionDisableMemoizeModel
{
  @Memoize( readOutsideTransaction = Feature.DISABLE )
  public long getTime()
  {
    return 0;
  }
}
