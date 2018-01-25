package com.example.computed_value_ref;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.ComputedValueRef;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class DuplicateRefMethodModel
{
  @Computed
  public long getTime()
  {
    return 0;
  }

  @Nonnull
  @ComputedValueRef
  public abstract arez.ComputedValue getTimeComputedValue();

  @ComputedValueRef( name = "time" )
  public abstract arez.ComputedValue getTimeComputedValue2();
}
