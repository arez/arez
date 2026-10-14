package com.example.observable_value_ref.other;

import arez.ObservableValue;
import arez.annotations.Observable;
import arez.annotations.ObservableValueRef;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class BaseProtectedAccessObservableValueRefModel
{
  @Observable
  public abstract long getTime();

  public abstract void setTime( long time );

  @NonNull
  @ObservableValueRef
  protected abstract ObservableValue<Long> getTimeObservableValue();
}
