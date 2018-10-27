package com.example.package_access.other;

import arez.ComputableValue;
import arez.annotations.Computed;
import arez.annotations.ComputedValueRef;
import javax.annotation.Nonnull;

public abstract class BaseComputedValueRefModel
{
  @Computed
  public long getTime()
  {
    return 0;
  }

  @Nonnull
  @ComputedValueRef
  abstract ComputableValue<Long> getTimeComputableValue();
}
