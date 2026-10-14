package com.example.computable_value_ref;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.ComputableValueRef;
import arez.annotations.Memoize;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class MultiComputableValueRefModel
{
  @Memoize
  public long getTime()
  {
    return 0;
  }

  @NonNull
  @ComputableValueRef
  abstract ComputableValue<Long> getTimeComputableValue();

  @ComputableValueRef( name = "time" )
  abstract @Nullable ComputableValue<Long> getTimeComputableValue2();
}
