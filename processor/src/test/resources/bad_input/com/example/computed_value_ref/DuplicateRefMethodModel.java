package com.example.computed_value_ref;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.ComputedValueRef;
import javax.annotation.Nonnull;

@ArezComponent
public class DuplicateRefMethodModel
{
  @Computed
  public long getTime()
  {
    return 0;
  }

  @Nonnull
  @ComputedValueRef
  public arez.ComputedValue getTimeComputedValue()
  {
    throw new IllegalStateException();
  }

  @ComputedValueRef( name = "time" )
  public arez.ComputedValue getTimeComputedValue2()
  {
    throw new IllegalStateException();
  }
}
