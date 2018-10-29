package com.example.computed_value_ref;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.ComputableValueRef;

@ArezComponent
public abstract class ParametersModel
{
  @Computed
  public long getTime()
  {
    return 0;
  }

  @ComputableValueRef
  abstract ComputableValue getTimeComputedValue( int i );
}
