package com.example.computed_value_ref;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.ComputedValueRef;

@ArezComponent
public class ParametersModel
{
  @Computed
  public long getTime()
  {
    return 0;
  }

  @ComputedValueRef
  arez.ComputedValue getTimeComputedValue( int i )
  {
    throw new IllegalStateException();
  }
}
