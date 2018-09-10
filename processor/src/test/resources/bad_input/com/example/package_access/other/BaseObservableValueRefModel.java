package com.example.package_access.other;

import arez.ObservableValue;
import arez.annotations.Observable;
import arez.annotations.ObservableValueRef;
import javax.annotation.Nonnull;

public abstract class BaseObservableValueRefModel
{
  @Observable
  public abstract String getMyValue();

  public abstract void setMyValue( String value );

  @Nonnull
  @ObservableValueRef
  abstract ObservableValue<String> getMyValueObservableValue();
}
