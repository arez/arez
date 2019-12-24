package com.example.computable_value_ref.other;

import arez.ComputableValue;
import arez.annotations.ComputableValueRef;
import arez.annotations.Memoize;
import javax.annotation.Nonnull;

public abstract class BaseProtectedAccessComputableValueRefModel
{
  @Memoize
  public long getTime()
  {
    return 0;
  }

  @Nonnull
  @ComputableValueRef
  protected abstract ComputableValue<Long> getTimeComputableValue();
}
