package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.DepType;
import arez.annotations.Feature;
import arez.annotations.Memoize;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( defaultWriteOutsideTransaction = Feature.ENABLE )
abstract class UnnecessaryDefaultWriteOutsideTransactionComponentModel
{
  @Memoize( depType = DepType.AREZ_OR_NONE )
  int getValue()
  {
    return 0;
  }
}
