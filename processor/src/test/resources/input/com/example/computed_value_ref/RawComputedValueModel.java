package com.example.computed_value_ref;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.ComputedValueRef;

@ArezComponent
public abstract class RawComputedValueModel
{
  @Computed
  public long getTime()
  {
    return 0;
  }

  @ComputedValueRef
  abstract ComputableValue getTimeComputableValue();
}
