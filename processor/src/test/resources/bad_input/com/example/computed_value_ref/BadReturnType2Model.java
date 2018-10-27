package com.example.computed_value_ref;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.ComputedValueRef;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class BadReturnType2Model
{
  @Computed
  public long getTime()
  {
    return 0;
  }

  @Nonnull
  @ComputedValueRef
  public abstract ComputableValue<?> getTimeComputableValue();
}
