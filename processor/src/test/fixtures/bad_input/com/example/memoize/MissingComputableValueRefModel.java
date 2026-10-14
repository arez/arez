package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.DepType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class MissingComputableValueRefModel
{
  @Memoize( depType = DepType.AREZ_OR_EXTERNAL )
  public @Nullable Integer getValue()
  {
    return null;
  }
}
