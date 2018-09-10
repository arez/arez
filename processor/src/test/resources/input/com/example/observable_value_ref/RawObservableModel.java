package com.example.observable_value_ref;

import arez.ObservableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableValueRef;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class RawObservableModel
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
  public abstract ObservableValue getTimeObservableValue();
}
