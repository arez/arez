package com.example.computable_value_ref;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.ComputableValueRef;
import arez.annotations.Memoize;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class MemoizeHasDifferentParameters4Model
{
  @Memoize
  public long getTime( int i, @Nullable String s, @Nullable Object o )
  {
    return 0;
  }

  @ComputableValueRef
  abstract @Nullable ComputableValue<Long> getTimeComputableValue( int i, @Nullable String s, @Nullable String o );
}
