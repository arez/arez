package com.example.computable_value_ref;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.ComputableValueRef;
import arez.annotations.Computed;

@ArezComponent
public abstract class NonStandardNameModel
{
  @Computed
  public long getTime()
  {
    return 0;
  }

  @ComputableValueRef( name = "time" )
  abstract ComputableValue timeComputableValue();
}
