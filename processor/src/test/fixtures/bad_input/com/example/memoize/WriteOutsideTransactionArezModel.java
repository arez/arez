package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Memoize;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class WriteOutsideTransactionArezModel
{
  @Memoize( writeOutsideTransaction = Feature.ENABLE )
  int value()
  {
    return 0;
  }
}
