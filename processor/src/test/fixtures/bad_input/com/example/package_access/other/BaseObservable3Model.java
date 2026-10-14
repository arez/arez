package com.example.package_access.other;

import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class BaseObservable3Model
{
  abstract @Nullable String getMyValue();

  @Observable
  public abstract void setMyValue( @Nullable String value );
}
