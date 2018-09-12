package com.example.observable_value_ref;

import arez.ObservableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableValueRef;
import java.text.ParseException;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class ThrowsExceptionModel
{
  @Observable
  public long getTime()
  {
    return 0;
  }

  public void setTime( final long time )
  {
  }

  @Nonnull
  @ObservableValueRef
  public abstract ObservableValue getTimeObservableValue()
    throws ParseException;
}
