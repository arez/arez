package com.example.computed_value_ref;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.ComputableValueRef;
import arez.annotations.Computed;
import java.text.ParseException;

@ArezComponent
public abstract class ThrowsExceptionModel
{
  @Computed
  public long getTime()
  {
    return 0;
  }

  @ComputableValueRef
  abstract ComputableValue getTimeComputableValue()
    throws ParseException;
}
