package com.example.package_access.other;

import arez.ComputableValue;
import arez.annotations.ComputableValueRef;
import arez.annotations.Memoize;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class BaseComputableValueRefModel
{
  @Memoize
  public long getTime()
  {
    return 0;
  }

  @NonNull
  @ComputableValueRef
  abstract ComputableValue<Long> getTimeComputableValue();
}
