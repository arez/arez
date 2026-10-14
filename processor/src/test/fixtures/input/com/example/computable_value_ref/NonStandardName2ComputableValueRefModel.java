package com.example.computable_value_ref;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.ComputableValueRef;
import arez.annotations.Memoize;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class NonStandardName2ComputableValueRefModel
{
  @Memoize
  public long getTime()
  {
    return 0;
  }

  @ComputableValueRef( name = "time" )
  abstract @Nullable ComputableValue<Long> $$$$timeComputableValue$$$$();
}
