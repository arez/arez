package com.example.observable_value_ref;

import arez.ObservableValue;
import arez.annotations.Observable;
import arez.annotations.ObservableValueRef;
import javax.annotation.Nonnull;

interface ObservableValueRefInterface
{
  @Observable
  long getTime();

  void setTime( long time );

  @Nonnull
  @ObservableValueRef
  ObservableValue<Long> getTimeObservableValue();
}
