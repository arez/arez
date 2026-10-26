package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.DepType;
import arez.annotations.Feature;
import arez.annotations.Memoize;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class WriteOutsideTransactionArezOrNoneModel
{
  @Memoize( depType = DepType.AREZ_OR_NONE, writeOutsideTransaction = Feature.ENABLE )
  int value()
  {
    return 0;
  }
}
