package com.example.computable_value_ref.other;

import arez.ComputableValue;
import arez.annotations.ComputableValueRef;
import arez.annotations.Memoize;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class BaseProtectedAccessComputableValueRefModel
{
  @Memoize
  public long getTime()
  {
    return 0;
  }

  @NonNull
  @ComputableValueRef
  protected abstract ComputableValue<Long> getTimeComputableValue();
}
