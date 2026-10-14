package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Memoize;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( defaultReadOutsideTransaction = Feature.DISABLE )
abstract class ReadOutsideTransactionFromDisabledDefaultMemoizeModel
{
  @Memoize
  public long getTime()
  {
    return 0;
  }
}
