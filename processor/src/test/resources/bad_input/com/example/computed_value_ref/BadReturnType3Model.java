package com.example.computed_value_ref;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.ComputableValueRef;
import arez.annotations.Computed;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class BadReturnType3Model
{
  @Computed
  public long getTime()
  {
    return 0;
  }

  @Nonnull
  @ComputableValueRef
  public abstract ComputableValue<String> getTimeComputableValue();
}
