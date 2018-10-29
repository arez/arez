package com.example.computed_value_ref;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.ComputableValueRef;

@ArezComponent
public abstract class StaticModel
{
  @Computed
  public long getTime()
  {
    return 0;
  }

  @ComputableValueRef
  static ComputableValue getTimeComputableValue()
  {
    throw new IllegalStateException();
  }
}
