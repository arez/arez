package com.example.computable_value_ref;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.ComputableValueRef;
import arez.annotations.Memoize;

@ArezComponent
public abstract class ParametersComputableValueRefModel
{
  @Memoize
  public long getTime( int i, String s, Object o )
  {
    return 0;
  }

  @ComputableValueRef
  abstract ComputableValue<Long> getTimeComputableValue( int i, String s, Object o );
}
