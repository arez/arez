package com.example.observable_value_ref;

import arez.ObservableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableValueRef;
import java.text.ParseException;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
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

  @SuppressWarnings( { "RedundantThrows", "RedundantSuppression" } )
  @NonNull
  @ObservableValueRef
  public abstract ObservableValue<Long> getTimeObservableValue()
    throws ParseException;
}
