package com.example.computable_value_ref;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.ComputableValueRef;
import arez.annotations.Memoize;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class MemoizeHasParametersModel
{
  @Memoize
  public long getTime( int i )
  {
    return 0;
  }

  @ComputableValueRef
  abstract @Nullable ComputableValue<Long> getTimeComputableValue();
}
