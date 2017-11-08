package com.example.computed_value_ref;

import javax.annotation.Nonnull;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.ComputedValueRef;

@ArezComponent
public class NonAlignedNameModel
{
  @Computed
  public long getTime()
  {
    return 0;
  }

  @Nonnull
  @ComputedValueRef
  public org.realityforge.arez.ComputedValue timeComputedValue()
  {
    throw new IllegalStateException();
  }
}
