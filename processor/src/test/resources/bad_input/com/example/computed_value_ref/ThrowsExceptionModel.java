package com.example.computed_value_ref;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.ComputedValueRef;
import java.text.ParseException;

@ArezComponent
public abstract class ThrowsExceptionModel
{
  @Computed
  public long getTime()
  {
    return 0;
  }

  @ComputedValueRef
  abstract arez.ComputedValue getTimeComputedValue()
    throws ParseException;
}
