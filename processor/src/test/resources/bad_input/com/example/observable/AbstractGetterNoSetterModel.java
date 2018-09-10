package com.example.observable;

import arez.ObservableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableValueRef;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class AbstractGetterNoSetterModel
{
  @Observable( expectSetter = false )
  public abstract long getField();

  @Nonnull
  @ObservableValueRef
  public abstract ObservableValue getFieldObservableValue();
}
