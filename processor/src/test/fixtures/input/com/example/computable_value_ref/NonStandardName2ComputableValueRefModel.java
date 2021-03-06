package com.example.computable_value_ref;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.ComputableValueRef;
import arez.annotations.Memoize;

@ArezComponent
abstract class NonStandardName2ComputableValueRefModel
{
  @Memoize
  public long getTime()
  {
    return 0;
  }

  @ComputableValueRef( name = "time" )
  abstract ComputableValue<Long> $$$$timeComputableValue$$$$();
}
