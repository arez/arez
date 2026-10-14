package com.example.observable;

import arez.ObservableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableValueRef;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class AbstractGetterNoSetterModel
{
  @Observable( expectSetter = false )
  public abstract long getField();

  @NonNull
  @ObservableValueRef
  public abstract ObservableValue<Long> getFieldObservableValue();
}
