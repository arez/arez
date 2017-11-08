package com.example.computed_value_ref;

import javax.annotation.Nonnull;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.ComputedValueRef;
import org.realityforge.arez.annotations.Observable;
import org.realityforge.arez.annotations.ObservableRef;

@ArezComponent
public class NonStandardNameModel
{
  @Computed
  public long getTime()
  {
    return 0;
  }

  @ComputedValueRef(name = "time")
  org.realityforge.arez.ComputedValue timeComputedValue()
  {
    throw new IllegalStateException();
  }
}
