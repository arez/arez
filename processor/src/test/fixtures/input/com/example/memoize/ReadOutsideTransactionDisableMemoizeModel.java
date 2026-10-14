package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Memoize;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class ReadOutsideTransactionDisableMemoizeModel
{
  @Memoize( readOutsideTransaction = Feature.DISABLE )
  public long getTime()
  {
    return 0;
  }
}
