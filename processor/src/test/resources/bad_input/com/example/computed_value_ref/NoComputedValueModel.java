package com.example.computed_value_ref;

import javax.annotation.Nonnull;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ComputedValueRef;

@ArezComponent
public class NoComputedValueModel
{
  @Nonnull
  @ComputedValueRef
  public org.realityforge.arez.ComputedValue getTimeComputedValue()
  {
    throw new IllegalStateException();
  }
}
