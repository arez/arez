package com.example.computable_value_ref;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.ComputableValueRef;
import arez.annotations.Memoize;
import javax.annotation.Nonnull;

@ArezComponent
abstract class PublicAccessViaInterfaceComputableValueRefModel
  implements ComputableValueRefInterface
{
  @Memoize
  public long getTime()
  {
    return 0;
  }

  @Override
  @Nonnull
  @ComputableValueRef
  public abstract ComputableValue<Long> getTimeComputableValue();
}
