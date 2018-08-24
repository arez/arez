package com.example.computed;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.ComputedValueRef;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class NonArezDependenciesComputedModel
{
  @Computed( arezOnlyDependencies = false )
  public long getTime()
  {
    return 0;
  }

  @Nonnull
  @ComputedValueRef
  abstract arez.ComputedValue<Long> getTimeComputedValue();
}
