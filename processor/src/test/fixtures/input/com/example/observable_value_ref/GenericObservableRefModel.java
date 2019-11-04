package com.example.observable_value_ref;

import arez.ObservableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableValueRef;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class GenericObservableRefModel
{
  public interface MyValue<T>
  {
  }

  @Observable
  public MyValue<String> getMyValue()
  {
    return null;
  }

  public void setMyValue( final MyValue<String> time )
  {
  }

  @Nonnull
  @ObservableValueRef
  public abstract ObservableValue<MyValue<String>> getMyValueObservableValue();
}
