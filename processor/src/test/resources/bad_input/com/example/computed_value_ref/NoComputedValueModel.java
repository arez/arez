package com.example.computed_value_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComputedValueRef;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class NoComputedValueModel
{
  @Nonnull
  @ComputedValueRef
  public abstract arez.ComputedValue getTimeComputedValue();
}
