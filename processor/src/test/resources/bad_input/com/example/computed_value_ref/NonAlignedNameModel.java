package com.example.computed_value_ref;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.ComputableValueRef;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class NonAlignedNameModel
{
  @Computed
  public long getTime()
  {
    return 0;
  }

  @Nonnull
  @ComputableValueRef
  public abstract ComputableValue timeComputableValue();
}
