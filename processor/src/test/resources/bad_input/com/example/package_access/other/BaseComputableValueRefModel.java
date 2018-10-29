package com.example.package_access.other;

import arez.ComputableValue;
import arez.annotations.Computed;
import arez.annotations.ComputableValueRef;
import javax.annotation.Nonnull;

public abstract class BaseComputableValueRefModel
{
  @Computed
  public long getTime()
  {
    return 0;
  }

  @Nonnull
  @ComputableValueRef
  abstract ComputableValue<Long> getTimeComputableValue();
}
