package com.example.computable_value_ref;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.ComputableValueRef;
import arez.annotations.Memoize;

@ArezComponent
public abstract class StaticModel
{
  @Memoize
  public long getTime()
  {
    return 0;
  }

  @ComputableValueRef
  static ComputableValue<Long> getTimeComputableValue()
  {
    throw new IllegalStateException();
  }
}
