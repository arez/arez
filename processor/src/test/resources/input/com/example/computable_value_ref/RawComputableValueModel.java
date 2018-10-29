package com.example.computable_value_ref;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.ComputableValueRef;

@ArezComponent
public abstract class RawComputableValueModel
{
  @Computed
  public long getTime()
  {
    return 0;
  }

  @ComputableValueRef
  abstract ComputableValue getTimeComputableValue();
}
