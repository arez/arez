package com.example.computable_value_ref;

import arez.ComputableValue;
import arez.annotations.ComputableValueRef;
import arez.annotations.Memoize;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface ComputableValueRefInterface
{
  @Memoize
  default long getTime()
  {
    return 0;
  }

  @NonNull
  @ComputableValueRef
  ComputableValue<Long> getTimeComputableValue();
}
