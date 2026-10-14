package com.example.observable_value_ref;

import arez.ObservableValue;
import arez.annotations.Observable;
import arez.annotations.ObservableValueRef;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
interface ObservableValueRefInterface
{
  @Observable
  long getTime();

  void setTime( long time );

  @NonNull
  @ObservableValueRef
  ObservableValue<Long> getTimeObservableValue();
}
