package com.example.observable_value_ref;

import arez.ObservableValue;
import arez.annotations.ArezComponent;
import arez.annotations.ObservableValueRef;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class NoObservableModel
{
  @NonNull
  @ObservableValueRef
  public abstract ObservableValue<Long> getTimeObservableValue();
}
