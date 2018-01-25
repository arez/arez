package com.example.computed_value_ref;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.ComputedValueRef;

@ArezComponent
public abstract class StaticModel
{
  @Computed
  public long getTime()
  {
    return 0;
  }

  @ComputedValueRef
  static arez.ComputedValue getTimeComputedValue()
  {
    throw new IllegalStateException();
  }
}
