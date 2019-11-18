package com.example.computable_value_ref;

import arez.ComputableValue;
import arez.annotations.ComputableValueRef;
import arez.annotations.Memoize;
import javax.annotation.Nonnull;

public interface ComputableValueRefInterface
{
  @Memoize
  default long getTime()
  {
    return 0;
  }

  @Nonnull
  @ComputableValueRef
  ComputableValue<Long> getTimeComputableValue();
}
