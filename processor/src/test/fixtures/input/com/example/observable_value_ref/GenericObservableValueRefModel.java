package com.example.observable_value_ref;

import arez.ObservableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableValueRef;
import javax.annotation.Nonnull;

@ArezComponent
abstract class GenericObservableValueRefModel
{
  interface MyValue<T>
  {
  }

  @Observable
  public abstract MyValue<String> getMyValue();

  public abstract void setMyValue( MyValue<String> time );

  @Nonnull
  @ObservableValueRef
  abstract ObservableValue<MyValue<String>> getMyValueObservableValue();
}
