package com.example.computed_value_ref;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.ComputedValueRef;

@ArezComponent
public class NonStandardNameModel
{
  @Computed
  public long getTime()
  {
    return 0;
  }

  @ComputedValueRef(name = "time")
  arez.ComputedValue timeComputedValue()
  {
    throw new IllegalStateException();
  }
}
