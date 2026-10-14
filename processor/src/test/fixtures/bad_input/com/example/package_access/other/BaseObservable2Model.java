package com.example.package_access.other;

import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class BaseObservable2Model
{
  @Observable
  public abstract @Nullable String getMyValue();

  abstract void setMyValue( @Nullable String value );
}
