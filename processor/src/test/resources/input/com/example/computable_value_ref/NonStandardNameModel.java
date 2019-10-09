package com.example.computable_value_ref;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.ComputableValueRef;
import arez.annotations.Memoize;

@ArezComponent
public abstract class NonStandardNameModel
{
  @Memoize
  public long getTime()
  {
    return 0;
  }

  @ComputableValueRef( name = "time" )
  abstract ComputableValue<Long> timeComputableValue();
}
