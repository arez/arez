package com.example.computable_value_ref;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.ComputableValueRef;
import arez.annotations.Memoize;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class DuplicateRefMethodModel
{
  @Memoize
  public long getTime()
  {
    return 0;
  }

  @Nonnull
  @ComputableValueRef
  public abstract ComputableValue getTimeComputableValue();

  @ComputableValueRef( name = "time" )
  public abstract ComputableValue getTimeComputableValue2();
}
