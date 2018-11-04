package com.example.computable_value_ref;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.ComputableValueRef;
import arez.annotations.Memoize;

@ArezComponent
public abstract class MemoizeHasDifferentParameters1Model
{
  @Memoize
  public long getTime( int i )
  {
    return 0;
  }

  @ComputableValueRef
  abstract ComputableValue getTimeComputableValue();
}
