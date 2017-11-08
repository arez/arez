package com.example.computed_value_ref;

import javax.annotation.Nonnull;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.ComputedValueRef;
import org.realityforge.arez.annotations.Observable;
import org.realityforge.arez.annotations.ObservableRef;

@ArezComponent
public class DuplicateRefMethodModel
{
  @Computed
  public long getTime()
  {
    return 0;
  }

  @Nonnull
  @ComputedValueRef
  public org.realityforge.arez.ComputedValue getTimeComputedValue()
  {
    throw new IllegalStateException();
  }

  @ComputedValueRef( name = "time" )
  public org.realityforge.arez.ComputedValue getTimeComputedValue2()
  {
    throw new IllegalStateException();
  }
}
