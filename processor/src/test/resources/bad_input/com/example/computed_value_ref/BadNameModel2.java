package com.example.computed_value_ref;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.ComputedValueRef;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class BadNameModel2
{
  @Computed
  public long getTime()
  {
    return 0;
  }

  @Nonnull
  @ComputedValueRef( name = "private" )
  abstract ComputableValue getTimeComputableValue();
}
