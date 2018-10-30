package com.example.computed_value_ref;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.ComputableValueRef;
import arez.annotations.Computed;

@ArezComponent
public abstract class PrivateModel
{
  @Computed
  public long getTime()
  {
    return 0;
  }

  @ComputableValueRef
  private ComputableValue getTimeComputableValue()
  {
    throw new IllegalStateException();
  }
}
