package com.example.computed_value_ref;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.ComputedValueRef;

@ArezComponent
public abstract class PrivateModel
{
  @Computed
  public long getTime()
  {
    return 0;
  }

  @ComputedValueRef
  private ComputableValue getTimeComputableValue()
  {
    throw new IllegalStateException();
  }
}
