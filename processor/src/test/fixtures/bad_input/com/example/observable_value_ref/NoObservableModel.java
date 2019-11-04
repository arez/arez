package com.example.observable_value_ref;

import arez.ObservableValue;
import arez.annotations.ArezComponent;
import arez.annotations.ObservableValueRef;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class NoObservableModel
{
  @Nonnull
  @ObservableValueRef
  public abstract ObservableValue<Long> getTimeObservableValue();
}
