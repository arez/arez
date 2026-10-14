package com.example.computable_value_ref;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.ComputableValueRef;
import arez.annotations.Memoize;
import java.text.ParseException;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class ThrowsExceptionModel
{
  @Memoize
  public long getTime()
  {
    return 0;
  }

  @SuppressWarnings( { "RedundantThrows", "RedundantSuppression" } )
  @ComputableValueRef
  abstract @Nullable ComputableValue<Long> getTimeComputableValue()
    throws ParseException;
}
