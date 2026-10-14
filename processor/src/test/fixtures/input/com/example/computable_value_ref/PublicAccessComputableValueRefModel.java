package com.example.computable_value_ref;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.ComputableValueRef;
import arez.annotations.Memoize;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class PublicAccessComputableValueRefModel
{
  @Memoize
  public long getTime()
  {
    return 0;
  }

  @NonNull
  @ComputableValueRef
  public abstract ComputableValue<Long> getTimeComputableValue();
}
