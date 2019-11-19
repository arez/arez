package com.example.observable_value_ref.other;

import arez.ObservableValue;
import arez.annotations.Observable;
import arez.annotations.ObservableValueRef;
import javax.annotation.Nonnull;

public abstract class BaseProtectedAccessObservableValueRefModel
{
  @Observable
  public abstract long getTime();

  public abstract void setTime( long time );

  @Nonnull
  @ObservableValueRef
  protected abstract ObservableValue<Long> getTimeObservableValue();
}
