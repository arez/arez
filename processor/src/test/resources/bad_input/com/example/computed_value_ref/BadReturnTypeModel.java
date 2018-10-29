package com.example.computed_value_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComputableValueRef;
import arez.annotations.Computed;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class BadReturnTypeModel
{
  @Computed
  public long getTime()
  {
    return 0;
  }

  @Nonnull
  @ComputableValueRef
  abstract String getTimeComputableValue();
}
