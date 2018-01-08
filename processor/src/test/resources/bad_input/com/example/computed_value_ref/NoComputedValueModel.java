package com.example.computed_value_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComputedValueRef;
import javax.annotation.Nonnull;

@ArezComponent
public class NoComputedValueModel
{
  @Nonnull
  @ComputedValueRef
  public arez.ComputedValue getTimeComputedValue()
  {
    throw new IllegalStateException();
  }
}
