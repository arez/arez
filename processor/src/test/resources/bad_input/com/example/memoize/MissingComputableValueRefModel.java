package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.DepType;

@ArezComponent
public abstract class MissingComputableValueRefModel
{
  @Memoize( depType = DepType.AREZ_OR_EXTERNAL )
  public Integer getValue()
  {
    return null;
  }
}
