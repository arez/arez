package com.example.computed;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.ComputableValueRef;
import arez.annotations.Computed;
import arez.annotations.DepType;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class NonArezDependenciesComputedModel
{
  @Computed( depType = DepType.AREZ_OR_EXTERNAL )
  public long getTime()
  {
    return 0;
  }

  @Nonnull
  @ComputableValueRef
  abstract ComputableValue<Long> getTimeComputableValue();
}
