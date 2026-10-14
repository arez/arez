package com.example.computable_value_ref;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.ComputableValueRef;
import arez.annotations.Memoize;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class RawComputableValueRefModel
{
  @Memoize
  public long getTime()
  {
    return 0;
  }

  @SuppressWarnings( "rawtypes" )
  @ComputableValueRef
  abstract @Nullable ComputableValue getTimeComputableValue();
}
