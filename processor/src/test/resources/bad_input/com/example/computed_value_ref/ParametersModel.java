package com.example.computed_value_ref;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.ComputedValueRef;

@ArezComponent
public class ParametersModel
{
  @Computed
  public long getTime()
  {
    return 0;
  }

  @ComputedValueRef
  org.realityforge.arez.ComputedValue getTimeComputedValue( int i )
  {
    throw new IllegalStateException();
  }
}
