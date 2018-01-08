package com.example.computed_value_ref;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.ComputedValueRef;
import javax.annotation.Nonnull;

@ArezComponent
public class FinalModel
{
  @Computed
  public long getTime()
  {
    return 0;
  }

  @Nonnull
  @ComputedValueRef
  final arez.ComputedValue getTimeComputedValue()
  {
    throw new IllegalStateException();
  }
}
