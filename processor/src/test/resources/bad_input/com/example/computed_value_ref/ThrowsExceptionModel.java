package com.example.computed_value_ref;

import java.text.ParseException;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.ComputedValueRef;

@ArezComponent
public class ThrowsExceptionModel
{
  @Computed
  public long getTime()
  {
    return 0;
  }

  @ComputedValueRef
  org.realityforge.arez.ComputedValue getTimeComputedValue()
    throws ParseException
  {
    throw new IllegalStateException();
  }
}
