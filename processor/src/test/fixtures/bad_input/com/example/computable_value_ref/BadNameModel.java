package com.example.computable_value_ref;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.ComputableValueRef;
import arez.annotations.Memoize;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class BadNameModel
{
  @Memoize
  public long getTime()
  {
    return 0;
  }

  @NonNull
  @ComputableValueRef( name = "-ace" )
  abstract ComputableValue<Long> getTimeComputableValue();
}
