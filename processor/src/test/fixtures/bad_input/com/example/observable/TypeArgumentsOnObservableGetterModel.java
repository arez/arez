package com.example.observable;

import arez.ObservableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableValueRef;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class TypeArgumentsOnObservableGetterModel
{
  @Observable( expectSetter = false )
  public <T> T getField()
  {
    return null;
  }

  @SuppressWarnings( "rawtypes" )
  @Nonnull
  @ObservableValueRef
  public abstract ObservableValue getFieldObservableValue();
}
