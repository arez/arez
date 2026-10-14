package com.example.observable_value_ref;

import arez.ObservableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableValueRef;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class GenericObservableValueRefModel
{
  @NullMarked
  interface MyValue<T extends @Nullable Object>
  {
  }

  @Observable
  public abstract @Nullable MyValue<String> getMyValue();

  public abstract void setMyValue( @Nullable MyValue<String> time );

  @NonNull
  @ObservableValueRef
  abstract ObservableValue<@Nullable MyValue<String>> getMyValueObservableValue();
}
