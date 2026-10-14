package com.example.observable;

import arez.ObservableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableValueRef;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class TypeArgumentsOnObservableGetterModel
{
  @Observable( expectSetter = false )
  public <T extends @Nullable Object> @Nullable T getField()
  {
    return null;
  }

  @SuppressWarnings( "rawtypes" )
  @NonNull
  @ObservableValueRef
  public abstract ObservableValue getFieldObservableValue();
}
