package com.example.package_access.other;

import arez.ObservableValue;
import arez.annotations.Observable;
import arez.annotations.ObservableValueRef;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class BaseObservableValueRefModel
{
  @Observable
  public abstract @Nullable String getMyValue();

  public abstract void setMyValue( @Nullable String value );

  @NonNull
  @ObservableValueRef
  abstract ObservableValue<String> getMyValueObservableValue();
}
